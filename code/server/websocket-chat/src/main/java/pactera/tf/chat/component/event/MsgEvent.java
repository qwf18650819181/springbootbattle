package pactera.tf.chat.component.event;

import org.springframework.context.ApplicationEvent;

import pactera.tf.chat.component.message.MessageBo;

/**
 * 点对点消息 事件
 * @author Lxy
 *
 */
public class MsgEvent extends ApplicationEvent{

	private MessageBo message;
	
	public MsgEvent(Object source,MessageBo message) {
		super(source);
		this.message=message;
	}
	
	public MessageBo getMessage() {
		return  message;
	}
}
