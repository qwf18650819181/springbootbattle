package pactera.tf.chatClient.trade;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;
import org.springframework.web.client.RestTemplate;

import pactera.tf.chatClient.compent.Servercommunication;
import pactera.tf.chatClient.dto.GroupDto;
import pactera.tf.chatClient.dto.MessageType;
import pactera.tf.chatClient.dto.UserDto;
import pactera.tf.chatClient.exception.BizException;
import pactera.tf.chatClient.exception.NeedLoginException;
import pactera.tf.chatClient.kafka.KafKaSender;
import pactera.tf.chatClient.utils.ApplicationUtils;
import pactera.tf.chatClient.utils.ResponseCode;

/**
 * 当前登录用户
 * @author lxy
 *
 */
public class LoginUser {

	public final static String ONLINE="1";
	public final static String OFFLINE="2";
	
	private String userCode;
	private String userName;
	
	private String nickName;
	private List<Friend> friends;
	private List<Group> groups;
	
	private ScheduledExecutorService excutorService=Executors.newSingleThreadScheduledExecutor();
	
	public String getUserCode() {
		return userCode;
	}
	public String getUserName() {
		return userName;
	}
	public String getNickName() {
		return nickName;
	}
	private LoginUser() {}
	/**
	 * 登录
	 * @param _userCode
	 * @param _userName
	 * @param _nickName
	 */
	public LoginUser(String _userCode,String _userName,String _nickName,List<UserDto> _friends,List<GroupDto> _groups) {
		userCode=_userCode;
		userName=_userName;
		nickName=_nickName;
		if(friends==null) {
			friends=new ArrayList<>();
		}
		for (UserDto dto : _friends) {
			Friend friend=new Friend();
			friend.setNickName(dto.getNickName());
			friend.setUserCode(dto.getUserCode());
			friend.setFlag(dto.getFlag());
			friends.add(friend);
		}
		if(groups==null) {
			groups=new ArrayList<>();
		}
		for (GroupDto groupDto : _groups) {
			Group group=new Group();
			group.setNickName(groupDto.getNickName());
			group.setGroupId(groupDto.getGroupId());
			group.setMasterCode(groupDto.getMastercode());
			group.setCnt(groupDto.getCnt());
		}
	}
	

	/**
	 * 加好友
	 * @param nickName 好友昵称
	 * @param userCode  好友code
	 * @param flag    好友在线标志
	 */
	public void addFriend(String nickName,String userCode,String flag) {
		Friend friend=new Friend();
		friend.setFlag(flag);
		friend.setNickName(nickName);
		friend.setUserCode(userCode);
		if(friends==null)
			friends=new ArrayList<Friend>();
		friends.add(friend);
	}
	
	/**
	 * 返回所有群组的topic
	 * @return
	 */
	public List<String> getGroupsTopic() {
		List<String> topics=new ArrayList<>();
		for (Group group : groups) {
			String topic=TopicNameGenerator.getGroupTopic(String.valueOf(group.getGroupId()));
			topics.add(topic);
		}
		
		return topics;
	}
	
	/**
	 * 返回自身的topic
	 * @return
	 */
	public String getTopic() {
		return TopicNameGenerator.getUserTopic(userCode);
	}
	
	/**
	 * 登录以后开始心跳包
	 * @throws NeedLoginException 
	 */
	public void headbeat() {
		//心跳 每30秒 发一次
		excutorService.scheduleAtFixedRate(new Runnable() {
			
			@Override
			public void run() {
				Servercommunication communication=ApplicationUtils.getInstance().getBean("servercommunication",Servercommunication.class);
				try {
					System.out.print(userCode+"发送了心跳");
					communication.headBeat();
				} catch (NeedLoginException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.print("没有登录，停止了心跳");
					excutorService.shutdown();
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}, 10, 10, TimeUnit.SECONDS);
		
	}
	
	/**
	 * 下线
	 */
	public void offline() {
		if(!excutorService.isShutdown()) {
			excutorService.shutdown();
		}
	}
	
	
	public void sendMessage(String receiveCode,String message,MessageType type) {
		KafKaSender sender=ApplicationUtils.getInstance().getBean(KafKaSender.class);
		if(type==MessageType.NORAML_MSG) {
			//判断是否有有好友关系
			if(isFriend(receiveCode)) {
				sender.sendMessage(receiveCode, message, userCode);
				return;
			}
			throw new BizException(ResponseCode.FRIEND_MISS);
		}
	}
	
	public String getFriendName(String userCode) {
		if(friends!=null && friends.size()>0) {
			for (Friend friend : friends) {
				if(userCode.equals(friend.getUserCode())) {
					return friend.nickName;
				}
			}
		}
		return "";
	}
	
	private boolean isFriend(String userCode) {
		if(friends!=null && friends.size()>0) {
			for (Friend friend : friends) {
				if(userCode.equals(friend.getUserCode())) {
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	/**
	 * 好友 内部类
	 * @author Pactera
	 *
	 */
	class Friend{
		String userCode;
		String nickName;
		String flag;
		public String getUserCode() {
			return userCode;
		}
		public String getNickName() {
			return nickName;
		}
		public String getFlag() {
			return flag;
		}
		public void setUserCode(String userCode) {
			this.userCode = userCode;
		}
		public void setNickName(String nickName) {
			this.nickName = nickName;
		}
		public void setFlag(String flag) {
			this.flag = flag;
		}
	}
	
	/**
	 * 群组 内部类
	 * @author Pactera
	 *
	 */
	class Group{
		String nickName;
		int groupId;
		int cnt;
		String masterCode;
		List<Friend> members;
		public String getNickName() {
			return nickName;
		}
		public int getGroupId() {
			return groupId;
		}
		public int getCnt() {
			return cnt;
		}
		public List<Friend> getMembers() {
			return members;
		}
		public void setNickName(String nickName) {
			this.nickName = nickName;
		}
		public void setGroupId(int groupId) {
			this.groupId = groupId;
		}
		public void setCnt(int cnt) {
			this.cnt = cnt;
		}
		public void setMembers(List<Friend> members) {
			this.members = members;
		}
		public String getMasterCode() {
			return masterCode;
		}
		public void setMasterCode(String masterCode) {
			this.masterCode = masterCode;
		}
		
		
	}
}
