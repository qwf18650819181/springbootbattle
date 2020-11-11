package pactera.tf.chat.dto;

import java.util.List;

/**
 * 群组 DTO，返回群名，群主，成员列表
 * @author Administrator
 *
 */
public class GroupDto {

	String nickName;
	
	String mastercode;
	
	int cnt;
	
	List<UserDto> members;
	
	int groupId;

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMastercode() {
		return mastercode;
	}

	public void setMastercode(String mastercode) {
		this.mastercode = mastercode;
	}

	public int getCnt() {
		return cnt;
	}

	public void setCnt(int cnt) {
		this.cnt = cnt;
	}

	public List<UserDto> getMembers() {
		return members;
	}

	public void setMembers(List<UserDto> members) {
		this.members = members;
	}
	
	
}
