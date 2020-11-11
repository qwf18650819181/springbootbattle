package pactera.tf.chatClient.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pactera.tf.chatClient.dto.MessageType;
import pactera.tf.chatClient.trade.LoginHolder;
import pactera.tf.chatClient.utils.ResponseCode;
import pactera.tf.chatClient.utils.ResponseResult;
import pactera.tf.chatClient.utils.ResposeResultUtil;

@RestController
public class MessageController {

	@Autowired
	LoginHolder holder;
	/**
	 * 发消息
	 * @param request
	 * @return
	 */
	@MessageMapping("/sendNormalMsg")
	public ResponseResult sendMsg(@RequestBody Map params) {
		String receiveCode=params.get("roomid").toString();
		String msg=params.get("mes").toString();
		if(StringUtils.isEmpty(receiveCode)) {
			return ResposeResultUtil.createResponse(ResponseCode.RECEIVECODE_ISNULL);
		}
		if(StringUtils.isEmpty(msg)) {
			return ResposeResultUtil.createResponse(ResponseCode.MESSAGE_ISNULL);
		}
		
		holder.sendMessage(receiveCode, msg, MessageType.NORAML_MSG);
		return ResposeResultUtil.success(null);
	}
}
