package pactera.tf.chatClient.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import pactera.tf.chatClient.dto.MessageBo;
import pactera.tf.chatClient.trade.TopicNameGenerator;

@Component
public class KafKaSender {

	@Autowired
	KafkaTemplate<String, MessageBo> kafkaTemplate;

	
	public void sendMessage(String receiveCode,String message,String sendCode) {
		MessageBo bo=MessageBo.createNoramMsg(receiveCode,sendCode, message);
		String topic=TopicNameGenerator.getUserTopic(receiveCode);
		kafkaTemplate.send(topic,bo);
	}
}
