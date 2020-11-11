package pactera.tf.chatClient.trade;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pactera.tf.chatClient.dto.LoginDto;
import pactera.tf.chatClient.dto.MessageType;
import pactera.tf.chatClient.exception.NeedLoginException;
import pactera.tf.chatClient.kafka.KafkaConsumer;
import pactera.tf.chatClient.trade.thread.GroupThread;

/**
 * 保存登录用户的信息，和相关操作
 * @author lxy
 *
 */
@Component(value="loginHolder")
public class LoginHolder {
	private LoginUser  user=null;
	
	@Autowired
	KafkaConsumer consumer;
	
	/**
	 * 登录
	 * @param dto
	 */
	public void login(LoginDto dto) {
		user=new LoginUser(dto.getUserCode(), dto.getUserName(), dto.getNickName(), dto.getFriends() , dto.getGroupds());
		//发送心跳包
		user.headbeat();
		//开始监听
		consumer.subscribe(user.getUserCode());
	}
	
	/**
	 * 登出
	 */
	public void logout() {
		System.out.println("下线了！！！");
		if(user!=null) {
			user.offline();
			consumer.unsubscribe();
			user=null;
		}
	} 
	
	
	/**
	 * 返回登录用户需要监听的所有groupId
	 * @return
	 * @throws NeedLoginException 
	 */
	public List<String> groups() throws NeedLoginException{
		if(user==null) {
			throw new NeedLoginException();
		}
		return user.getGroupsTopic();
	}
	
	/**
	 * 返回登录用户的监听主题
	 * @return
	 * @throws NeedLoginException 
	 */
	public List<String> selfTopic() throws NeedLoginException{
		if(user==null) {
			throw new NeedLoginException();
		}
		List<String> topic=new ArrayList<String>();
		topic.add(user.getTopic());
		return topic;
	}
	
	public String getUserCode() throws NeedLoginException {
		if(user==null) {
			throw new NeedLoginException();
		}
		return user.getUserCode();
	}
	
	
	/**
	 * 发送消息
	 * @param receiveCode
	 * @param msg
	 * @param type
	 */
	public void sendMessage(String receiveCode,String msg,MessageType type) {
		user.sendMessage(receiveCode, msg, type);
	}
	
	public String getNickName(String userCode) {
		return user.getFriendName(userCode);
	}
}
