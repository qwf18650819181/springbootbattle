package pactera.tf.chatClient.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

@Configuration
@EnableWebSocketMessageBroker
public class ClientConfiguartion implements WebSocketMessageBrokerConfigurer{

	  @Autowired
	  private RestTemplateBuilder builder;

	    @Bean
	    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
	        //return builder.build();
	        RestTemplate restTemplate = new RestTemplate(factory);

	        //换上fastjson
	        List<HttpMessageConverter<?>> httpMessageConverterList= restTemplate.getMessageConverters();
	        Iterator<HttpMessageConverter<?>> iterator=httpMessageConverterList.iterator();
	        if(iterator.hasNext()){
	            HttpMessageConverter<?> converter=iterator.next();
	            //原有的String是ISO-8859-1编码 去掉
	            if(converter instanceof StringHttpMessageConverter){
	                iterator.remove();
	            }

	            //由于系统中默认有jackson 在转换json时自动会启用  但是我们不想使用它 可以直接移除或者将fastjson放在首位
	            /*if(converter instanceof GsonHttpMessageConverter || converter instanceof MappingJackson2HttpMessageConverter){
	                iterator.remove();
	            }*/

	        }
	        httpMessageConverterList.add(new StringHttpMessageConverter(Charset.forName("utf-8")));
	        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
	        FastJsonConfig fastJsonConfig = new FastJsonConfig();
	        fastJsonConfig.setSerializerFeatures(
	                SerializerFeature.WriteMapNullValue,
	                SerializerFeature.WriteNullStringAsEmpty,
	                SerializerFeature.WriteNullListAsEmpty,
	                SerializerFeature.DisableCircularReferenceDetect);

	        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
	        List<MediaType> supportedMediaTypes = new ArrayList<>();
	        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
	        supportedMediaTypes.add(MediaType.APPLICATION_ATOM_XML);
	        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
	        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
	        supportedMediaTypes.add(MediaType.APPLICATION_PDF);
	        supportedMediaTypes.add(MediaType.APPLICATION_RSS_XML);
	        supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
	        supportedMediaTypes.add(MediaType.APPLICATION_XML);
	        supportedMediaTypes.add(MediaType.IMAGE_GIF);
	        supportedMediaTypes.add(MediaType.IMAGE_JPEG);
	        supportedMediaTypes.add(MediaType.IMAGE_PNG);
	        supportedMediaTypes.add(MediaType.TEXT_EVENT_STREAM);
	        supportedMediaTypes.add(MediaType.TEXT_HTML);
	        supportedMediaTypes.add(MediaType.TEXT_MARKDOWN);
	        supportedMediaTypes.add(MediaType.TEXT_PLAIN);
	        supportedMediaTypes.add(MediaType.TEXT_XML);
	        fastJsonHttpMessageConverter.setSupportedMediaTypes(supportedMediaTypes);
	        httpMessageConverterList.add(0,fastJsonHttpMessageConverter);

	        return restTemplate;

	    }

	    @Bean	
	    @ConditionalOnMissingBean({ClientHttpRequestFactory.class})
	    public ClientHttpRequestFactory requestFactory() {
	        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
	        //ms
	        factory.setConnectTimeout(15000);
	        factory.setReadTimeout(15000);
	        return factory;
	    }
	    
	    @Override
	    public void configureMessageBroker(MessageBrokerRegistry registry) {
	    	registry.enableSimpleBroker("/msg");
	    	registry.setApplicationDestinationPrefixes("/server");
		} 
	    
	    @Override
	    public void registerStompEndpoints(StompEndpointRegistry registry) {
	    	registry.addEndpoint("/dist/chat").setAllowedOrigins("*").withSockJS();
		}
}
