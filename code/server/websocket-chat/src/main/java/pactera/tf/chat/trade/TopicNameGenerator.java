package pactera.tf.chat.trade;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicNameGenerator {

	@Resource(name="kafkaAdminClient")
	KafkaAdminClient adminClient;
	
	private int numPartitions=1;
	private short replicationFactor=1;
	
	public String getUserTopic(String usercode) {
		return "MSG_"+usercode;
	}
	
	/**
	 * 创建用户聊天topic
	 * @param usercode
	 */
	public void createUserTopic(String usercode) {
		createTopic(getUserTopic(usercode));
	}
	
	private void createTopic(String name) {
		String topicname=name;
		NewTopic topic=new NewTopic(topicname, numPartitions,replicationFactor);
		List<NewTopic> list=new ArrayList<NewTopic>();
		list.add(topic);
		adminClient.createTopics(list);
	}
	
	public String getGroupTopic(String id) {
		return "GROUP_"+id;
	}
	
	/**
	 * 创建 群主体
	 * @param id
	 */
	public void createGroupTopic(String id) {
		createTopic(getGroupTopic(id));
	}
	
	public String getThumbUpTopic(String usercode) {
		return "THUMB_"+usercode;
	}
	
	/**
	 * 创建 群主体
	 * @param id
	 */
	public void createThumbUpTopic(String usercode) {
		createTopic(getThumbUpTopic(usercode));
	}
	
	
}
