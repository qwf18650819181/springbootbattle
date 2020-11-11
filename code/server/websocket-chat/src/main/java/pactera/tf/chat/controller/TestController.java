package pactera.tf.chat.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSON;


import io.swagger.annotations.ApiOperation;
import pactera.tf.chat.component.message.MessageBo;
import pactera.tf.chat.dto.RegisterUserDto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import pactera.tf.chat.redis.RedisClient;
import pactera.tf.chat.utils.ResponseCode;
import pactera.tf.chat.utils.ResponseResult;
import pactera.tf.chat.utils.ResposeResultUtil;

@RestController
public class TestController {
	
	@Autowired 
    public StringRedisTemplate stringRedisTemplate;
	@Autowired 
	private KafkaTemplate<String, MessageBo> kafkaTemplate;
//	@Autowired
//	SysUserService userService;

	@Resource(name="kafkaAdminClient")
    private AdminClient adminClient;

	
	@ApiOperation(value = "登陆入口", notes = "登陆逻辑")
    @RequestMapping(value = "/testLogin", method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult<String> login() {
		ResponseResult<String> response = null;
        try {
            response = ResposeResultUtil.createResponse(ResponseCode.SUCCESS, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
	
	
//	@RequestMapping("/testRedis")
//    @ResponseBody
//    public String getUrl(String messages , String username){
////        stringRedisTemplate.convertAndSend("test" , messages);
////        String demo = RedisClient.returnMessage();
////        return "客户端接收到的的消息:"+demo;
//    }
	
	@RequestMapping("/testKafka")
	public void sendMsg(@RequestParam String message,@RequestParam String topic) {
		MessageBo bo=MessageBo.createNoramMsg("1991", "123", message);
		kafkaTemplate.send(topic, bo);
	}

	@RequestMapping("/testGroupKafka")
	public void sendGroupMsg(@RequestParam String message) {
		MessageBo bo=MessageBo.createGroupMsg("774307","1991", message);
		kafkaTemplate.send("GROUP_774307", bo);
	}
	
	
	@ApiOperation(value = "创建topic")
    @ApiImplicitParams({
           @ApiImplicitParam(name = "topicName", value = "topic名称",defaultValue = "first_top",
                    required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "partitions", value = "分区数", defaultValue = "4",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "replicationFactor", value = "副本数", defaultValue = "1",
                    required = true, dataType = "int", paramType = "query")
    })
    @GetMapping("/createTopic")
    public String createTopic(String topicName,int partitions,int replicationFactor){
        adminClient.createTopics(Arrays.asList(new NewTopic(topicName,partitions,(short)replicationFactor)));
        return "create success";
    }

	@RequestMapping(value="/testRegister",method = RequestMethod.POST)
	public ResponseResult<JSONObject> register(@Valid @RequestBody RegisterUserDto user) {
//		if(bindResult.hasErrors()) {
//			String msg=bindResult.getFieldError().getDefaultMessage();
//			return ResposeResultUtil.error("1005", msg);
//		}

		//userService.register("123",user.getUserName(), user.getPasswd(), user.getNick(), user.getEmail());

//		userService.register("123",user.getUserName(), user.getPasswd(), user.getNick(), user.getEmail());

//		userService.register("123",user.getUserName(), user.getPasswd(), user.getNick(), user.getEmail());

		JSONObject object=new JSONObject();
		object.put("usercode", "123");
		return ResposeResultUtil.success(object);
	}
	
	@ApiOperation(value = "查看所有的topic")
    @GetMapping("/findAllTopic")
    public String findAllTopic() throws ExecutionException, InterruptedException {
        ListTopicsResult result = adminClient.listTopics();
        Collection<TopicListing> list = result.listings().get();
        List<String> resultList = new ArrayList<>();
        for(TopicListing topicListing : list){
            resultList.add(topicListing.name());
        }
        return JSON.toJSONString(resultList);
    }
	
	@ApiOperation(value = "查看topic详情")
    @ApiImplicitParams({
           @ApiImplicitParam(name = "topicName", value = "topic名称",defaultValue = "first_top",
                    required = true, dataType = "string", paramType = "query")
    })
    @GetMapping("/infoTopic")
    public String topicInfo(String topicName) throws ExecutionException, InterruptedException {
        DescribeTopicsResult result = adminClient.describeTopics(Arrays.asList(topicName));
        Map<String,String> resultMap = new HashMap<>();
        result.all().get().forEach((k,v)->{
            resultMap.put(k,v.toString());
        });

        return JSON.toJSONString(resultMap);
    }
	
	@ApiOperation(value = "删除topic")
    @ApiImplicitParams({
           @ApiImplicitParam(name = "topicName", value = "topic名称",defaultValue = "first_top",
                    required = true, dataType = "string", paramType = "query")
    })
    @GetMapping("/deleteTopic")
    public String deleteTopic(String topicName){
        DeleteTopicsResult  result = adminClient.deleteTopics(Arrays.asList(topicName));
        return  JSON.toJSONString(result.values());
    }
	
	

}
