package pactera.tf.chat.component;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pactera.tf.chat.trade.TopicNameGenerator;
import pactera.tf.chat.trade.chart.IGroupChatService;
import pactera.tf.chat.trade.friend.AuthenService;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 保存 系统内 群组 和 个人聊天 使用的所有topics，和相关操作 
 * @author Pactera
 *
 */
@Component
public class ChatTopic implements InitializingBean{	
	
	@Autowired
	AuthenService userService;
	
	@Autowired
	IGroupChatService  groupChatService;
	
	@Autowired
	TopicNameGenerator nameGenerator;
	
	Set<String> chatTopics;
	
	Set<String> groupTopics;
	
	public Set<String> getChatTopics() {
		return chatTopics;
	}

	public Set<String> getGroupTopics() {
		return groupTopics;
	}

	public ChatTopic() {
		chatTopics=new CopyOnWriteArraySet<String>();
		groupTopics=new CopyOnWriteArraySet<String>();
	}
	
	/**
	 * 添加 个人聊天 主题
	 * @param topic
	 */
	public void addChatTopic(String topic) {
		chatTopics.add(topic);
	}
	
	/**
	 * 删除个人聊天的主题
	 * @param topic
	 */
	public void deleteChatTopic(String topic) {
		chatTopics.remove(topic);
	}
	
	/**
	 * 新加群组 topic
	 * @param topic
	 */
	public void addGroupTopic(String topic) {
		groupTopics.add(topic);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// 重启的时候，默认 加入所有的 人 和 群组 主题
		List<String> usercodes=userService.selectAllUser();
		for (String usercode : usercodes) {
			chatTopics.add(nameGenerator.getUserTopic(usercode));
		}
		
		
		List<String> groups=groupChatService.selectAllGroup();
		for (String id : groups) {
			groupTopics.add(nameGenerator.getGroupTopic(id));
		}
		
	}
	
	

}
