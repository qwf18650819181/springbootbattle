package pactera.tf.chat.kafka;

import org.apache.kafka.common.serialization.Deserializer;

import com.alibaba.fastjson.JSONObject;

import pactera.tf.chat.component.message.MessageBo;

public class MessageBoDeserializer implements Deserializer<MessageBo>{

	@Override
	public MessageBo deserialize(String topic, byte[] data) {
		String msg=new String(data);
		return JSONObject.parseObject(msg, MessageBo.class);
	}
	

}
