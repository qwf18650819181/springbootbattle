package pactera.tf.chat.component.message;

import org.springframework.util.StringUtils;

import pactera.tf.chat.entity.BuMessageDo;

/**
 * 离线消息Bo
 * @author lxy
 *
 */
public class MessageBo {
	
	/**
	 * 系统用户常量
	 */
	private final static String SYSTEM="system";

	private MessageType msgtype;
	private String from;
	private String realFrom;
	
	private String to;
	private String message;
	
	public MessageType getMsgtype() {
		return msgtype;
	}

	public String getFrom() {
		return from;
	}

	public String getRealFrom() {
		return realFrom;
	}

	public String getTo() {
		return to;
	}

	public String getMessage() {
		return message;
	}
	
	//以下的set方法，纯粹是为了 兼容fastjosn 转换JSON格式 而做的妥协。按理来说 不应该暴露这些set方法。
	public void setMsgtype(MessageType msgtype) {
		this.msgtype = msgtype;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setRealFrom(String realFrom) {
		this.realFrom = realFrom;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private MessageBo(MessageType _msgType,String _sendCode,String _receiveCode,String _realySendCode,String _message){
		msgtype=_msgType;
		to=_receiveCode;
		from=_sendCode;
		realFrom=_realySendCode;
		message=_message;
	}
	
	public MessageBo() {}
	
	/**
	 * 创建
	 * @param receiveCode
	 * @param sendcode
	 * @param message
	 * @return
	 */
	public static MessageBo createNoramMsg(String receiveCode,String sendcode,String message) {
		MessageBo bo=new MessageBo(MessageType.NORAML_MSG,sendcode,receiveCode,"",message);
		return bo;
	}
	
	/**
	 * 创建 群组消息
	 * @param receiveCode
	 * @param realReceiveCode
	 * @param sendcode
	 * @param message
	 * @return
	 */
	public static MessageBo createGroupMsg(String receiveCode,String realSendCode,String message) {
		MessageBo bo=new MessageBo(MessageType.GROUP_MSG, "", receiveCode, realSendCode, message);
		return bo;
	}
	
	/**
	 * 创建加好友消息
	 */
	public static MessageBo createAddFriendMsg(String receiveCode,String fromCode,String message) {
		MessageBo bo=new MessageBo(MessageType.ADDFRIEND_MSG, SYSTEM, receiveCode, fromCode, message);
		return bo;
	}
	
	/**
	 * 点赞消息
	 * @param receiveCode
	 * @param realSndCode
	 * @param message
	 * @return
	 */
	public static MessageBo createGoodMsg(String receiveCode,String realSndCode,String message) {
		MessageBo bo=new MessageBo(MessageType.GOOD_MSG, SYSTEM, receiveCode, realSndCode, message);
		return bo;
	}
	
	/**
	 * 整点报时消息
	 * @param receiveCode
	 * @param message
	 * @return
	 */
	public static MessageBo createTimeMsg(String receiveCode,String message) {
		MessageBo bo=new MessageBo(MessageType.TIME_MSG, SYSTEM, receiveCode, "", message);
		return bo;
	}
	
	/**
	 * 好友通过消息
	 * @param receiveCode
	 * @param message
	 * @return
	 */
	public static MessageBo createFriendPassMsg(String receiveCode,String message) {
		MessageBo bo=new MessageBo(MessageType.FRIENDPASS_MSG, SYSTEM, receiveCode, "", message);
		return bo;
	}
	
	/**
	 * 加入群聊消息
	 * @param receiveCode
	 * @param message
	 * @return
	 */
	public static MessageBo createJoinGroup(String receiveCode,String message) {
		MessageBo bo=new MessageBo(MessageType.JOIN_GROUP,SYSTEM,receiveCode,"",message);
		return bo;
	}
	
	public static MessageBo coverDo2Bo(BuMessageDo message) {
		MessageBo bo=new MessageBo();
		bo.setFrom(message.getSendcode());
		bo.setRealFrom(message.getRealsendcode());
		bo.setTo(message.getReceivecode());
		bo.setMessage(message.getMessage());
		for (MessageType msgType : MessageType.values()) {
            if(msgType.getCode()==Integer.parseInt(message.getMsgtype())) {
            	bo.setMsgtype(msgType);
            }
        }
		return bo;
	}
}