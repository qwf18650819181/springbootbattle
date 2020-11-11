package pactera.tf.chatClient.trade.thread;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.ApplicationEventPublisher;

import pactera.tf.chatClient.dto.MessageBo;
import pactera.tf.chatClient.exception.NeedLoginException;
import pactera.tf.chatClient.trade.LoginHolder;
import pactera.tf.chatClient.trade.event.GroupEvent;
import pactera.tf.chatClient.utils.ApplicationUtils;

public class GroupThread extends Thread{
	Consumer<String, MessageBo> consumer;
	boolean flag;
	LoginHolder holder;
	boolean startSub;
	
	public GroupThread(Consumer<String, MessageBo> _consumer) {
		consumer=_consumer;
		flag=true;
		holder=ApplicationUtils.getInstance().getBean("loginHolder", LoginHolder.class);
		
	}
	
	public void run() {
		List<String> groupOldTopics=new ArrayList<String>();
		startSub=false;
		while(flag) {
			try {
				//运行中 新加入的topic 在下一次循环的时候在监听
	        	List<String> newTopics=new ArrayList<>(holder.groups());
	        	//群组和个人有不同，个人会取消监听，群组不会取消，只会一直追加
	        	//newTopics 和oldTopics 取差，new 肯定比 old多
	        	newTopics.removeAll(groupOldTopics);
	        	if(newTopics.size()>0) {
	        		startSub=true;
	        		consumer.subscribe(newTopics);
	        		consumer.poll(0);
	        		//新加入的主题都要从最后开始监听
	        		List<TopicPartition> tp=new ArrayList<>();
					//只有本次变动的主题 才要调到最后
					for (String string : newTopics) {
						tp.add(new TopicPartition(string, 0));
					}
					if(tp.size()>0) {
						consumer.seekToEnd(tp);
					}
	            	String topiclist="";
	            	for (String string : newTopics) {
						topiclist+=string+";";
					}
	            	System.out.println("群组聊天，开始监听新群组"+topiclist);
	        	}
	        	if(!startSub) {
	        		continue;
	        	}
	        	 ConsumerRecords<String, MessageBo> records = consumer.poll(Duration.ofMillis(100));
	             for (ConsumerRecord<String, MessageBo> record : records) {
	                 //收到消息 ，发送接收消息事件
	            	 GroupEvent event=new GroupEvent(this, record.value());
	            	 ApplicationUtils.getInstance().publish(event);
	             }
	        	//本次 的 topic 成为 旧topics
	        	groupOldTopics.addAll(newTopics);
			}catch (NeedLoginException e) {
				e.printStackTrace();
				//掉线了 就要退出
				break;
			}catch (Exception e) {
				e.printStackTrace();
				//保证继续运行
			}
		}
		consumer.unsubscribe();
		consumer.close();
	}
	
	
	/**
	 * 停止
	 */
	public void stopSelf() {
		flag=false;
		holder=null;
	}
}
