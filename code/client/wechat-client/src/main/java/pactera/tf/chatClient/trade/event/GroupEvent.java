package pactera.tf.chatClient.trade.event;

import org.springframework.context.ApplicationEvent;

import pactera.tf.chatClient.dto.MessageBo;

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
