package pactera.tf.chatClient.trade;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class TopicNameGenerator {
	
	public static String getUserTopic(String usercode) {
		return "MSG_"+usercode;
	}
	
	public static String getGroupTopic(String id) {
		return "GROUP_"+id;
	}
	
}
