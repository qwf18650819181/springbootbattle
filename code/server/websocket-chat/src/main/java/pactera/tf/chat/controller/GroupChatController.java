package pactera.tf.chat.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pactera.tf.chat.dto.GroupDto;
import pactera.tf.chat.dto.UserDto;
import pactera.tf.chat.entity.BuGroupDo;
import pactera.tf.chat.entity.BuGroupUserKeyDo;
import pactera.tf.chat.entity.SysUserDo;
import pactera.tf.chat.mapper.SysUserMapper;
import pactera.tf.chat.trade.chart.IGroupChatService;
import pactera.tf.chat.trade.chart.Impl.GroupChatServiceImpl;
import pactera.tf.chat.utils.ResponseCode;
import pactera.tf.chat.utils.ResponseResult;
import pactera.tf.chat.utils.ResposeResultUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Api(tags = "群聊")
@RestController
public class GroupChatController {

    @Autowired
    private IGroupChatService iGroupChatService;
    
    @Autowired
    public StringRedisTemplate redis;
    
    @Autowired
    SysUserMapper userMapper;

    @ApiOperation(value = "群聊列表", notes = "群聊列表查询")
    @RequestMapping(value = "/groupChatList", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "usercode", value = "用户id", required = true)
    })
    public ResponseResult groupChatList(@RequestParam String usercode){
       return iGroupChatService.groupDoList(usercode);
    }

    @ApiOperation(value = "创建群聊", notes = "创建群聊")
    @RequestMapping(value = "/createGroupChat", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "usercode", value = "用户id", required = true),
            @ApiImplicitParam(name = "nickname", value = "群名称", required = true),
            @ApiImplicitParam(name = "idString", value = "群人员id,分号分割", required = true)
    })
    public ResponseResult createGroupChat(@RequestParam String idString,@RequestParam String usercode,@RequestParam String nickname){
    	
    	String[] groupers=idString.split(";");
    	if(groupers.length<2) {
    		return ResposeResultUtil.createResponse(ResponseCode.GROUP_MAST_TOW);
    	}
        return iGroupChatService.createGroupChat(Arrays.asList(groupers),usercode,nickname);
    }

    @ResponseBody
    @RequestMapping(value = "/joinGroup/{groupId}",method = RequestMethod.POST)
    @ApiOperation(value="加入群聊",notes = "创建群聊")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "groupId", value = "群组id", required = true),
        @ApiImplicitParam(name = "userCode", value = "用户code", required = true)
    })
    public ResponseResult<GroupDto> joinGroup(@PathVariable("groupId") String groupId,@RequestParam String userCode){
    	//先判断 groupid 是否存在
    	BuGroupDo group=iGroupChatService.selectGroupInfo(groupId);
    	if(group==null) {
    		return ResposeResultUtil.createResponse(ResponseCode.GROUP_MISS);
    	}
    	//加入群聊
    	if(iGroupChatService.joinGroup(groupId, userCode)) {
    		//返回群组信息
    		List<BuGroupUserKeyDo> members=iGroupChatService.selectMembers(groupId);
    		//组装成 dto
    		List<UserDto> userDtos=new ArrayList<>();
    		for (BuGroupUserKeyDo userKey : members) {
				UserDto dto=new UserDto();
				SysUserDo user=userMapper.selectByPrimaryKey(userKey.getUsercode());
				dto.setNickName(user.getNick());
				if(redis.hasKey(userKey.getUsercode()+GroupChatServiceImpl.LOGIN_KEY)) {
					dto.setFlag(UserDto.ONLINE);
				}else {
					dto.setFlag(UserDto.OFFLINE);
				}
				userDtos.add(dto);
			}
    		GroupDto groupDto=new GroupDto();
    		groupDto.setCnt(group.getCnt());
    		groupDto.setMastercode(group.getMastercode());
    		groupDto.setMembers(userDtos);
    		groupDto.setNickName(group.getNickname());
    		groupDto.setGroupId(group.getId());
    		return ResposeResultUtil.success(groupDto);
    	}else {
    		return ResposeResultUtil.createResponse(ResponseCode.GROUP_JOIN_FAIL);
    	}
    	
    }

	@ApiOperation(value = "群组信息", notes = "群组信息查询逻辑")
	@ResponseBody
	@PostMapping(value="/groupInfo/{groupId}")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "groupId", value = "群组id", required = true),
			@ApiImplicitParam(name = "flag", value = "返回群组用户信息标识", required = true)
	})
	public ResponseResult searchGroupInfo(@PathVariable("groupId") String groupId, @RequestParam boolean flag) {
		if (StringUtils.isEmpty(groupId)){
			return ResposeResultUtil.createResponse(ResponseCode.ILLEGAL_ARGUMENT);
		}
		return iGroupChatService.searchGroupInfo(groupId, flag);
	}

}
