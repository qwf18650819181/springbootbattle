package pactera.tf.chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import pactera.tf.chat.entity.BuFriendListDo;
import pactera.tf.chat.entity.SysUserDo;
import pactera.tf.chat.mapper.SysUserMapper;
import pactera.tf.chat.trade.chart.IFriendService;
import pactera.tf.chat.utils.ResponseCode;
import pactera.tf.chat.utils.ResponseResult;
import pactera.tf.chat.utils.ResposeResultUtil;

import javax.annotation.Resource;

@Api(tags = "好友")
@RestController
public class FriendController {

    @Autowired
    private IFriendService iFriendService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    public StringRedisTemplate redis;

    public static final String ADD_FRIEND_KEY="_friend_";

    @ApiOperation(value = "好友列表", notes = "好友列表查询")
    @RequestMapping(value = "/friendList", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "usercode", value = "用户id", required = true)
    })
    public ResponseResult friendList(@RequestParam String usercode){
        return iFriendService.friendList(usercode);
    }


    @ApiOperation(value = "添加好友", notes = "添加好友")
    @RequestMapping(value = "/addFriend", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "usercode", value = "用户id", required = true),
            @ApiImplicitParam(name = "friendcode", value = "添加对象id", required = true),
            @ApiImplicitParam(name = "msg", value = "验证信息", required = true)
    })
    public ResponseResult addFriend(@RequestParam String usercode ,@RequestParam String friendcode,@RequestParam String msg){
        BuFriendListDo buFriendListDo = iFriendService.findFrindByFriendcode(usercode,friendcode);
        if(buFriendListDo !=null){
            return ResposeResultUtil.createResponse(ResponseCode.FRIEND_JOIN_FALL);
        }
        String redisKey = "A"+usercode+ADD_FRIEND_KEY+"B"+friendcode;
        if(redis.hasKey(redisKey)){
            return ResposeResultUtil.createResponse(ResponseCode.FRIEND_JOIN_WAIT);
        }
        SysUserDo sysUserDo =  sysUserMapper.selectByPrimaryKey(friendcode);
        if(sysUserDo ==null){
            return ResposeResultUtil.createResponse(ResponseCode.FRIEND_INEXISTENCE);
        }
       return iFriendService.addFriend(usercode,friendcode,msg,redisKey);
    }
    @ApiOperation(value = "同意好友请求", notes = "同意好友请求")
    @RequestMapping(value = "/agreeRequest", method = RequestMethod.POST)
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "usercode", value = "用户id", required = true),
            @ApiImplicitParam(name = "requestcode", value = "请求对象id", required = true),
            @ApiImplicitParam(name = "msg", value = "验证信息回复", required = true)
    })
    public ResponseResult agreeRequest(@RequestParam String usercode ,@RequestParam String requestcode ,@RequestParam String msg){
        String redisKey = "A"+requestcode+ADD_FRIEND_KEY+"B"+usercode;
        return iFriendService.agreeRequest(usercode,requestcode,msg,redisKey);
    }

}
