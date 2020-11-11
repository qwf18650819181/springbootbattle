package pactera.tf.chatClient.trade.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 群组消息 离线缓存
 * @author Pactera
 *
 */
@Component
public class GroupEventListener implements ApplicationListener<GroupEvent>{

//	@Autowired
//	IGroupChatService groupService;
//	
//	@Autowired
//	IBuMessageService buMessageService;
	@Override
	public void onApplicationEvent(GroupEvent event) {
//		//每个群组消息 都需要判断群组内是否有用户不在线，不在线就要缓存
//		MessageBo message=event.getMessage();
//		List<String> userCodes=groupService.checkGroup(message.getFrom());
//		buMessageService.createMessage(userCodes, message);
	}

}
