package pactera.tf.chat.redis;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * 接收信息的客户端
 * @author pactera
 *
 */
@Component
public class RedisClient {
	
	public static final String LOGIN_KEY="_login";
	@Resource(name="stringRedisTemplate")
	RedisTemplate<String,String> redis;
	 
	 private final int TIMEOUT_LENGTH=30;
	/**
	 * 判断客户是否在线
	 * @param userCode
	 * @return
	 */
	public boolean isLogin(String userCode) {
		return redis.hasKey(userCode+LOGIN_KEY);
	}
	
	
	/**
	 * 上线
	 * @param userCode
	 */
	public void login(String userCode,String ip) {
		Date now = new Date();
		redis.opsForValue().set(userCode+LOGIN_KEY, ip+","+now, TIMEOUT_LENGTH,TimeUnit.SECONDS);
	}
	
	/**
	 * 下线
	 * @param userCode
	 */
	public void logout(String userCode) {
		redis.delete(userCode+LOGIN_KEY);
	}
	
	/**
	 * 更新过期时间
	 * @param userCode
	 */
	public void expire(String userCode) {
		redis.expire(userCode+LOGIN_KEY, TIMEOUT_LENGTH,TimeUnit.SECONDS);
	}

}
