package pactera.tf.chat.dto;

import pactera.tf.chat.entity.SysUserDo;

/**
 * 好友dto，好友名，在线状态
 * @author Administrator
 *
 */
public class UserDto {

	String  nickName;
	String userCode;
	String  flag;
	
	public static final String ONLINE="1";
	public static final String OFFLINE="2";
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public UserDto() {
	}

	public UserDto(SysUserDo userDo) {
		this.userCode = userDo.getUsercode();
		this.nickName = userDo.getNick();
		this.flag = OFFLINE;
	}
}
