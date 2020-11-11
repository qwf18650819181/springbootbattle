package pactera.tf.chat.trade.chart;

import java.util.List;

import pactera.tf.chat.component.message.MessageBo;

public interface IBuMessageService {

	/**
	 * 批量离线缓存 ，用户 群组缓存
	 * @param userCodes
	 * @param message
	 * @return
	 */
	boolean createMessage(List<String> userCodes,MessageBo message);
	
	/**
	 * 单笔消息缓存，用于点对点
	 * @param bo
	 * @return
	 */
	boolean createMessage(String userCode,MessageBo bo);
}
