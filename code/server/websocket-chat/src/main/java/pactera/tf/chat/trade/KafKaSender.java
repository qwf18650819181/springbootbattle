package pactera.tf.chat.trade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import pactera.tf.chat.component.message.MessageBo;

@Component
public class KafKaSender {

	@Autowired
	TopicNameGenerator nameGenerator;
	@Autowired
	KafkaTemplate<String, MessageBo> kafkaTemplate;
	
	
	public void sendAddFriend(String fromCode,String toCode,String formUserName) {
		String message=formUserName+"("+fromCode+")请求添加你为好友";
		MessageBo bo=MessageBo.createAddFriendMsg(toCode,formUserName, message);
		String topic=nameGenerator.getUserTopic(fromCode);
		kafkaTemplate.send(topic,bo);
	}
	
	public void sendThumbUp(String fromCode,String toCode,String formUserName) {
		String message=formUserName+"("+fromCode+")点赞了你，快去回赞一个";
		MessageBo bo=MessageBo.createGoodMsg(toCode,fromCode,message);
		String topic=nameGenerator.getUserTopic(fromCode);
		kafkaTemplate.send(topic,bo);
	}
	
	public void sendTimeReport(String receiveCode,String message) {
		MessageBo bo=MessageBo.createTimeMsg(receiveCode, message);
		String topic=nameGenerator.getUserTopic(receiveCode);
		kafkaTemplate.send(topic,bo);
	}
	
	public void sendJoinGroup(String receiveCode,String message) {
		MessageBo bo=MessageBo.createJoinGroup(receiveCode, message);
		String topic=nameGenerator.getUserTopic(receiveCode);
		kafkaTemplate.send(topic,bo);
	}
}
