package pactera.tf.chatClient.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Holder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import io.netty.util.internal.StringUtil;
import pactera.tf.chatClient.compent.Servercommunication;
import pactera.tf.chatClient.dto.MessageType;
import pactera.tf.chatClient.exception.BizException;
import pactera.tf.chatClient.exception.NeedLoginException;
import pactera.tf.chatClient.trade.LoginHolder;
import pactera.tf.chatClient.utils.ResponseCode;
import pactera.tf.chatClient.utils.ResponseResult;
import pactera.tf.chatClient.utils.ResposeResultUtil;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	Servercommunication communication;
	
	@Autowired
	LoginHolder holder;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseResult login(@RequestBody Map params) {
		String userCode=params.get("userCode").toString();
		String password=params.get("password").toString();
		try {
			return communication.login(userCode, password);
		}catch (BizException e) {
			// TODO: handle exception
			return new ResponseResult<>(e.getErrorCode(), e.getErrorMsg());
		}
	}
	
	@RequestMapping(method = RequestMethod.POST,value="/logout")
	public ResponseResult logout() {
		
		try {
			return communication.logout();
		}catch (NeedLoginException e) {
			// TODO: handle exception
			return new ResponseResult<>(e.getCode(), e.getMsg());
		}
	}

}
