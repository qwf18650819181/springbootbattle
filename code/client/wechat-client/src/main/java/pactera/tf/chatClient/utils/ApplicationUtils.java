package pactera.tf.chatClient.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationUtils implements ApplicationContextAware{

	private static ApplicationContext context;
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context=applicationContext;
	}
	
	private ApplicationUtils() {}
	
	private static ApplicationUtils instance;
	
	public static ApplicationUtils getInstance() {
		if(instance==null)
			instance=new ApplicationUtils();
		return instance;
		
	}
	
	public <T> T getBean(String name,Class<T> type) {
		return (T)context.getBean(name, type);
	}
	
	public <T> T getBean(Class<T> type) {
		return (T)context.getBean(type);
	}
	
	public void publish(ApplicationEvent event){
		context.publishEvent(event);
	}
}
