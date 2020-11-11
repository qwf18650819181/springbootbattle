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
import pactera.tf.chatClient.trade.event.MsgEvent;
import pactera.tf.chatClient.utils.ApplicationUtils;

/**
 * 监控登录用户自身的topic
 * @author lxy
 *
 */
public class SelfThread extends Thread{
	Consumer<String, MessageBo> consumer;
	boolean flag;
	LoginHolder holder;
	
	public SelfThread(Consumer<String, MessageBo> _consumer) {
		consumer=_consumer;
		flag=true;
		holder=ApplicationUtils.getInstance().getBean("loginHolder", LoginHolder.class);
		
	}
	
	public void run() {
		List<String> topics;
		try {
			topics = holder.selfTopic();
			consumer.subscribe(topics);
			consumer.poll(0);
			List<TopicPartition> tps = Arrays.asList(new TopicPartition(topics.get(0), 0));
			//跳到最后的消息
			consumer.seekToEnd(tps);
			System.out.println("开始监听自身主题"+topics.get(0));
			while(flag) {
				try {
		        	 ConsumerRecords<String, MessageBo> records = consumer.poll(Duration.ofMillis(100));
		             for (ConsumerRecord<String, MessageBo> record : records) {
		                 //收到消息 ，发送接收消息事件
		            	 MsgEvent event=new MsgEvent(this, record.value());
		            	 
		            	 ApplicationUtils.getInstance().publish(event);
		             }
				}catch (Exception e) {
					e.printStackTrace();
					//保证继续运行
				}
			}
		} catch (NeedLoginException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
