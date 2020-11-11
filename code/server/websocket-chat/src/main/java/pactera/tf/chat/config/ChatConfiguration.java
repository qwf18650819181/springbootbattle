package pactera.tf.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pactera.tf.chat.utils.ApplicationUtils;

@Configuration
public class ChatConfiguration {

	/**
	 * 注册 ApplicationUtils ，方便DDD模型 获取 applicationContext
	 * @return
	 */
	@Bean
	public ApplicationUtils appUtil() {
		return ApplicationUtils.getInstance();
	}
	
}
