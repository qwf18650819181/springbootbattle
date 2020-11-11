package pactera.tf.chat.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import pactera.tf.chat.component.ChatTopic;
import pactera.tf.chat.trade.TopicNameGenerator;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

	@Autowired
	ChatTopic chatTopic;
	@Autowired
	TopicNameGenerator nameGenerator;
	
	
	 public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
	        super(listenerContainer);
	    }

	    public void onMessage(Message message, byte[] pattern) {
	        String expiredKey = message.toString();
	        System.out.println("redis key过期：{}" + expiredKey);
	        String usercode=expiredKey.replace(RedisClient.LOGIN_KEY, "");
	        //开启缓存
	        chatTopic.addChatTopic(nameGenerator.getUserTopic(usercode));
	    }
}
