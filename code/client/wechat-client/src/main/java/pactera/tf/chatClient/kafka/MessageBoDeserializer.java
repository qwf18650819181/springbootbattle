package pactera.tf.chatClient.kafka;

import java.io.UnsupportedEncodingException;

import org.apache.kafka.common.serialization.Deserializer;

import com.alibaba.fastjson.JSONObject;

import pactera.tf.chatClient.dto.MessageBo;

public class MessageBoDeserializer implements Deserializer<MessageBo>{

	@Override
	public MessageBo deserialize(String topic, byte[] data) {
		String msg="";
		try {
			msg = new String(data,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSONObject.parseObject(msg, MessageBo.class);
	}
	

}
