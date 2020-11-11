package pactera.tf.chat.component.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import pactera.tf.chat.component.message.MessageBo;
import pactera.tf.chat.entity.BuMessageDo;
import pactera.tf.chat.mapper.BuMessageMapper;
import pactera.tf.chat.trade.chart.IBuMessageService;

/**
 * 离线点对点消息
 * @author Pactera
 *
 */
@Component
public class MsgEventListener implements ApplicationListener<MsgEvent>{

	@Autowired
	IBuMessageService msgService;
	
	@Override
	public void onApplicationEvent(MsgEvent event) {
		MessageBo message=event.getMessage();
		msgService.createMessage(message.getTo(), message);
	}

	
}
