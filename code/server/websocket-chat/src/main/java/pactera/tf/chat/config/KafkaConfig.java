package pactera.tf.chat.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaConsumerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


import pactera.tf.chat.component.message.MessageBo;

import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Kafka 配置消息
 * @author lxy
 *
 */
@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
public class KafkaConfig{
	
	private final KafkaProperties properties;

	public KafkaConfig(KafkaProperties properties) {
		this.properties = properties;
	}
    
	@Bean
	public AdminClient kafkaAdminClient() {
		AdminClient adminClient = AdminClient.create(this.properties.buildAdminProperties());
		return adminClient;
	}

//	@Bean
//	public ConsumerFactory<String,MessageBo> kafkaConsumerFactory(
//			ObjectProvider<DefaultKafkaConsumerFactoryCustomizer> customizers) {
//		System.out.println("测试协议爱  ，水仙开始");
//		DefaultKafkaConsumerFactory<String,MessageBo> factory = new DefaultKafkaConsumerFactory<String,MessageBo>(
//				this.properties.buildConsumerProperties());
////		org.apache.kafka.common.serialization.StringSerializer ser=new org.apache.kafka.common.serialization.StringSerializer();
////		factory.setKeyDeserializer((String key,byte[] data)->{
////			
////		});
//		customizers.orderedStream().forEach((customizer) -> customizer.customize(factory));
//		return factory;
//	}

   
}
