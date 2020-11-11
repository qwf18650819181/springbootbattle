package pactera.tf.chatClient.compent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

import pactera.tf.chatClient.dto.LoginDto;
import pactera.tf.chatClient.dto.ResponseResultUserDto;
import pactera.tf.chatClient.dto.ResponseReusltLogin;
import pactera.tf.chatClient.dto.UserDto;
import pactera.tf.chatClient.exception.BizException;
import pactera.tf.chatClient.exception.NeedLoginException;
import pactera.tf.chatClient.trade.LoginHolder;
import pactera.tf.chatClient.utils.ResponseCode;
import pactera.tf.chatClient.utils.ResponseResult;


/**
 * 用于和服务器的接口 
 * @author lxy
 *
 */
@Component
public class Servercommunication {

	@Value("${chat.server.url}")
	String serverUrl;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	LoginHolder holder;
	
	/**
	 * 心跳包
	 * @return
	 * @throws NeedLoginException 
	 */
	public boolean headBeat() throws NeedLoginException {
		String userCode=holder.getUserCode();
		String url=serverUrl+"v/heartbeat/"+userCode;
		ResponseResultUserDto dtos=restTemplate.postForObject(url, null, ResponseResultUserDto.class);
		
		return true;
	}
	
	/**
	 * 登录
	 * @param userCode
	 * @param password
	 * @return
	 */
	public ResponseReusltLogin login(String userCode,String password) {
		String url=serverUrl+"v/user/login?usercodeOrEmail="+userCode+"&password="+password;
		ResponseReusltLogin response=restTemplate.postForObject(url, null,ResponseReusltLogin.class);
		if(!"0".equals(response.getCode())) {
			throw new BizException(response.getCode(), "服务端异常："+response.getMessage());
		}
		holder.login(response.getData());
		return response;
		
	}
	
	
	public ResponseResult  logout() throws NeedLoginException {
		String url=serverUrl+"v/logout?usercode="+holder.getUserCode();
		ResponseResult response=restTemplate.postForObject(url, null, ResponseResult.class);
		if(!"0".equals(response.getCode())) {
			throw new BizException(response.getCode(), "服务端异常："+response.getMessage());
		}
		holder.logout();
		return response;
	}
}
