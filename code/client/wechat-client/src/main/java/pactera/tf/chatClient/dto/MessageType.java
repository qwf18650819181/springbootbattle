package pactera.tf.chatClient.dto;

/**
 * 消息类型
 * @author lxy
 *
 */
public enum MessageType {

	/**
	 * 正常聊天消息
	 */
	NORAML_MSG(0),
	/**
	 * 群组消息
	 */
	GROUP_MSG(3),
	/**
	 * 加好友
	 */
	ADDFRIEND_MSG(1),
	/**
	 * 点赞消息
	 */
	GOOD_MSG(2),
	/**
	 * 整点消息
	 */
	TIME_MSG(4),
	/**
	 * 好友通过消息
	 */
	FRIENDPASS_MSG(5),
	/**
	 * 加入群聊
	 */
	JOIN_GROUP(6)
	;
	
	
	
	MessageType(int code) {
        this.code = code;
    }
	
	private int code;

	public int getCode() {
		return code;
	}
	
	

}
