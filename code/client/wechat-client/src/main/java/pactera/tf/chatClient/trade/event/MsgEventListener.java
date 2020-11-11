package pactera.tf.chatClient.trade.event;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import pactera.tf.chatClient.dto.MessageBo;
import pactera.tf.chatClient.dto.MessageType;
import pactera.tf.chatClient.trade.LoginHolder;

/**
 * 点对点聊天消息
 * @author lxy
 *
 */
@Component
public class MsgEventListener implements ApplicationListener<MsgEvent>{

	@Autowired
	SimpMessagingTemplate template;
	
	@Autowired
	LoginHolder holder;
//	
	@Override
	public void onApplicationEvent(MsgEvent event) {
		MessageBo messageBo=event.getMessage();
		System.out.println("收到点对点消息:"+JSON.toJSONString(messageBo));
		messageBo.setFrom(holder.getNickName(messageBo.getFrom()));
		if(messageBo.getMsgtype()==MessageType.NORAML_MSG) {
			template.convertAndSend("/msg/normal",JSON.toJSONString(messageBo));
		}
	}

	
}
