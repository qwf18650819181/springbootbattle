package pactera.tf.chat.component.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import pactera.tf.chat.component.message.MessageBo;
import pactera.tf.chat.trade.chart.IBuMessageService;
import pactera.tf.chat.trade.chart.IGroupChatService;

/**
 * 群组消息 离线缓存
 * @author Pactera
 *
 */
@Component
public class GroupEventListener implements ApplicationListener<GroupEvent>{

	@Autowired
	IGroupChatService groupService;
	
	@Autowired
	IBuMessageService buMessageService;
	@Override
	public void onApplicationEvent(GroupEvent event) {
		//每个群组消息 都需要判断群组内是否有用户不在线，不在线就要缓存
		MessageBo message=event.getMessage();
		List<String> userCodes=groupService.checkGroup(message.getTo());
		buMessageService.createMessage(userCodes, message);
	}

}
