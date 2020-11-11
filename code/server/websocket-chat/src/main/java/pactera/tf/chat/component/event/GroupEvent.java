package pactera.tf.chat.component.event;

import org.springframework.context.ApplicationEvent;

import pactera.tf.chat.component.message.MessageBo;

/**
 * 群组消息 事件
 * @author Lxy
 *
 */
public class GroupEvent extends ApplicationEvent{

	private MessageBo message;
	
	public GroupEvent(Object source,MessageBo message) {
		super(source);
		this.message=message;
	}
	
	public MessageBo getMessage() {
		return  message;
	}
}
