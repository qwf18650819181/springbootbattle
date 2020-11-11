package pactera.tf.chat.dto;

import java.util.List;

import pactera.tf.chat.component.message.MessageBo;

/**
 * 登录后返回的dto
 * @author Administrator
 *
 */
public class LoginDto {

	/**
	 * 昵称
	 */
	String nickName;
	/**
	 * 用户编码
	 */
	String userCode;
	/**
	 * 好友列表
	 */
	List<UserDto> friends;
	/**
	 * 群组信息
	 */
	List<GroupDto> groupds;
	/**
	 * 离线消息
	 */
	List<MessageBo> offlineMessages;
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public List<UserDto> getFriends() {
		return friends;
	}
	public void setFriends(List<UserDto> friends) {
		this.friends = friends;
	}
	public List<GroupDto> getGroupds() {
		return groupds;
	}
	public void setGroupds(List<GroupDto> groupds) {
		this.groupds = groupds;
	}
	public List<MessageBo> getOfflineMessages() {
		return offlineMessages;
	}
	public void setOfflineMessages(List<MessageBo> offlineMessages) {
		this.offlineMessages = offlineMessages;
	}
	
	
	
}
