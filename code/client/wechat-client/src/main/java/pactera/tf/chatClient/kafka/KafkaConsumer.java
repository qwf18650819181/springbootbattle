package pactera.tf.chatClient.kafka;

import java.util.List;

import javax.annotation.Resource;

import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

import pactera.tf.chatClient.dto.MessageBo;
import pactera.tf.chatClient.trade.LoginHolder;
import pactera.tf.chatClient.trade.thread.GroupThread;
import pactera.tf.chatClient.trade.thread.SelfThread;

/**
 * kafka 消费者，使用 原生的 consumer进行监听
 * @author lxy
 *
 */
@Component
public class KafkaConsumer {
	
	@Autowired
	LoginHolder loginHolder;
	
	@Resource
	ConsumerFactory<String,MessageBo> consumerFactory;
	
	SelfThread selfThread;
	GroupThread groupThread;
	
	/**
	 * 开启kafka监听
	 * @param userCode
	 */
	public void subscribe(String userCode) {
		Consumer<String, MessageBo> groupConsumer=consumerFactory.createConsumer("chat-consumer_"+userCode, "client_group"+userCode);
		Consumer<String, MessageBo> consumer=consumerFactory.createConsumer("chat-consumer_"+userCode, "client_"+userCode);
		selfThread=new SelfThread(consumer);
		groupThread=new GroupThread(groupConsumer);
		
		//开启监听
		selfThread.start();
		groupThread.start();
	}
	
	/**
	 * 停止监听
	 */
	public void unsubscribe() {
		selfThread.stopSelf();
		groupThread.stopSelf();
		
	}

}
