package pactera.tf.chat.component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.DependsOn;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

import pactera.tf.chat.component.event.GroupEvent;
import pactera.tf.chat.component.event.MsgEvent;
import pactera.tf.chat.component.message.MessageBo;

/**
 * kafka 群组,个人消息接收类
 * 不使用spring 自带的 @kafkalistener 的原因是  无法自定义的改动监听的主题。
 * 每次循环，判断是否有新建立的群组，有的话，就加入监听，获取到的监听数据，给 离线模块处理
 * @author Pactera
 *
 */
@Component
@DependsOn("chatTopic")
public class KafakGroupConsumer implements InitializingBean {

	@Autowired
	ChatTopic topics;
	
	@Autowired
    private ApplicationEventPublisher applicationEventPublisher;
	
	@Resource
	ConsumerFactory<String,MessageBo> consumerFactory;
	
	
	Consumer<String, MessageBo> consumer;
	Consumer<String, MessageBo> groupConsumer;
	List<String> oldTopics;
	List<String> groupOldTopics;

	public KafakGroupConsumer() {
		
	}
	
	private List<String> differ(List<String> list,List<String> old){
		if(list==null)
			return Collections.EMPTY_LIST;
		if(old==null)
			return list;
		if((list.size()==0) &&(old.size()==0)) {
			return Collections.EMPTY_LIST;
		}
		list.removeAll(old);
		return list;
	}
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		consumer=consumerFactory.createConsumer();
		oldTopics=new ArrayList<String>();
		
		groupConsumer=consumerFactory.createConsumer();
		groupOldTopics=new ArrayList<String>();
		//开启一个动态监听 kafka的线程，只监听 点对点的消息
		new Thread(() -> {
            while (true) {
            	//运行中 新加入的topic 在下一次循环的时候在监听
            	List<String> newTopics=new ArrayList<>(topics.getChatTopics());
            	List<String> copyTopic=new ArrayList<>(topics.getChatTopics());
            	if(!newTopics.equals(oldTopics)) {
	            	//先停止所有的主题监听，在开启新的监听
	            	consumer.unsubscribe();
	            	consumer.subscribe(newTopics);
	            	String topiclist="";
	            	for (String string : newTopics) {
						topiclist+=string+";";
					}
	            	System.out.println("个人聊天，开始监听"+topiclist);
	            	
	            	consumer.poll(0);//监听生效
					List<String> diff=differ(newTopics,oldTopics);
					List<TopicPartition> tp=new ArrayList<>();
					//只有本次变动的主题 才要调到最后
					for (String string : diff) {
						tp.add(new TopicPartition(string, 0));
					}
					if(tp.size()>0) {
						consumer.seekToEnd(tp);
					}
            	}
            	
            	 ConsumerRecords<String, MessageBo> records = consumer.poll(Duration.ofMillis(100));
                 for (ConsumerRecord<String, MessageBo> record : records) {
                     //收到消息 ，发送接收消息事件
                	 System.out.println(record.value());
                	 MsgEvent event=new MsgEvent(this, record.value());
                	 applicationEventPublisher.publishEvent(event);
                 }
               //本次 的 topic 成为 旧topics
                 oldTopics=copyTopic;
            }
		}).start();;
		//监听群组消息
		new Thread(() -> {
			boolean flag=false;//表示是否已经开始监听
            while (true) {
            	//运行中 新加入的topic 在下一次循环的时候在监听
            	List<String> newTopics=new ArrayList<>(topics.getGroupTopics());
            	//群组和个人有不同，个人会取消监听，群组不会取消，只会一直追加
            	//newTopics 和oldTopics 取差，new 肯定比 old多
            	newTopics.removeAll(groupOldTopics);
            	if(newTopics.size()>0) {
	            	//开启新的监听
            		flag=true;
            		groupConsumer.subscribe(newTopics);
	            	String topiclist="";
	            	for (String string : newTopics) {
						topiclist+=string+";";
					}
	            	System.out.println("群组聊天，开始监听新群组"+topiclist);
            	}else {
            		if(!flag)
            			continue;
            	}
            	
            	 ConsumerRecords<String, MessageBo> records = groupConsumer.poll(Duration.ofMillis(100));
                 for (ConsumerRecord<String, MessageBo> record : records) {
                     //收到消息 ，发送接收消息事件
                	 GroupEvent event=new GroupEvent(this, record.value());
                	 applicationEventPublisher.publishEvent(event);
                 }
            	//本次 的 topic 成为 旧topics
            	groupOldTopics.addAll(newTopics);
            }
		}).start();;
	}

}
