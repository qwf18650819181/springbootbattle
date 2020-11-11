package pactera.tf.chatClient.kafka;

import org.apache.kafka.common.serialization.Serializer;

import com.alibaba.fastjson.JSON;

import pactera.tf.chatClient.dto.MessageBo;

public class MessageBoSerializer implements Serializer<MessageBo>{

	@Override
	public byte[] serialize(String topic, MessageBo data) {
		String json=JSON.toJSONString(data);
		return json.getBytes();
	}

}
