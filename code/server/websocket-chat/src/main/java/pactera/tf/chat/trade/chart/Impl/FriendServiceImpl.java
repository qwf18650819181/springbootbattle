package pactera.tf.chat.trade.chart.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pactera.tf.chat.component.ChatTopic;
import pactera.tf.chat.component.message.MessageBo;
import pactera.tf.chat.dto.UserDto;
import pactera.tf.chat.entity.BuFriendListDo;
import pactera.tf.chat.entity.BuFriendListKey;
import pactera.tf.chat.entity.BuMessageDo;
import pactera.tf.chat.entity.SysUserDo;
import pactera.tf.chat.mapper.BuFriendListMapper;
import pactera.tf.chat.mapper.BuMessageMapper;
import pactera.tf.chat.mapper.SysUserMapper;
import pactera.tf.chat.redis.RedisClient;
import pactera.tf.chat.trade.KafKaSender;
import pactera.tf.chat.trade.TopicNameGenerator;
import pactera.tf.chat.trade.chart.IFriendService;
import pactera.tf.chat.utils.ResponseCode;
import pactera.tf.chat.utils.ResponseResult;
import pactera.tf.chat.utils.ResposeResultUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class FriendServiceImpl implements IFriendService {


    private static final Logger logger = LoggerFactory.getLogger(FriendServiceImpl.class);

    @Resource
    private BuFriendListMapper buFriendListMapper;

    @Autowired
    RedisClient redisClient;
    @Resource
    private SysUserMapper sysUserMapper;

    @Autowired
    public StringRedisTemplate redis;

    @Resource
    BuMessageMapper mapper;

    @Autowired
    TopicNameGenerator topciGenerator;

    @Autowired
    ChatTopic chatTopic;
    
    @Autowired
    KafKaSender sender;



    @Override
    public ResponseResult friendList(String usercode) {
        logger.info("当前登录的用户:", usercode);
        List<BuFriendListDo> friendList = buFriendListMapper.selectAllFriendsByUsercode(usercode);
        List<UserDto> friends=new ArrayList<UserDto>();
        for (BuFriendListDo friend: friendList){
            String friendCode = friend.getFriendcode();
            if (StringUtils.isEmpty(friendCode)){
                continue;
            }
            SysUserDo friendUserDo=sysUserMapper.selectByPrimaryKey(friendCode);
            UserDto dto=new UserDto();
            dto.setNickName(friendUserDo.getNick());
            dto.setUserCode(friendCode);
            if(redisClient.isLogin(friendUserDo.getUsercode())) {
                dto.setFlag(UserDto.ONLINE);
            }else {
                dto.setFlag(UserDto.OFFLINE);
            }
            friends.add(dto);
        }
        return ResposeResultUtil.createResponse(ResponseCode.SUCCESS,friends);
    }

    @Override
    public BuFriendListDo findFrindByFriendcode(String userCode, String friendcode) {
        BuFriendListKey BuFriendListKey = new BuFriendListKey();
        BuFriendListKey.setUsercode(userCode);
        BuFriendListKey.setFriendcode(friendcode);
        return buFriendListMapper.selectByPrimaryKey(BuFriendListKey);
    }

    @Override
    public ResponseResult addFriend(String usercode, String friendcode,String msg, String redisKey) {
        try{
            //插入好友添加消息
            int isSuccess =  mapper.insert(this.assembleEntity(usercode,friendcode,msg));

            //加入 系统监听
//            chatTopic.addGroupTopic(topciGenerator.getUserTopic(usercode));

            //redis存储信息
            if(!redis.hasKey(redisKey)) {
                redis.opsForValue().set(redisKey, "1");
            }
            redis.opsForValue().increment(redisKey);
            
            //发送 加好友请求
            SysUserDo user = sysUserMapper.selectByPrimaryKey(usercode);
            sender.sendAddFriend(usercode, friendcode,user.getNick());
            
            
            return ResposeResultUtil.createResponse(ResponseCode.SUCCESS);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResposeResultUtil.createResponse(ResponseCode.ERROR);
        }

    }

    @Override
    public ResponseResult agreeRequest(String usercode,String requestcode,String msg,String redisKey) {
        if(redis.hasKey(redisKey)) {
            BuMessageDo buMessageDo = new BuMessageDo();
            buMessageDo.setId(Integer.valueOf(this.createId(null)));
            buMessageDo.setReceivecode(requestcode);
            buMessageDo.setRealsendcode(usercode);
            buMessageDo.setSendcode("system");
            buMessageDo.setMessage(msg);
            buMessageDo.setMsgtype("5");
            buMessageDo.setIsreceive("N");
            //插入添加成功消息
            int isSuccess =  mapper.insert(buMessageDo);
            topciGenerator.createUserTopic(usercode);
            //删除redis
            redis.delete(redisKey);

            //好友列表添加
            BuFriendListDo buFriendListDo = new BuFriendListDo();
            buFriendListDo.setUsercode(usercode);
            buFriendListDo.setFriendcode(requestcode);
            buFriendListDo.setCreatetime(new Date());
            buFriendListMapper.insert(buFriendListDo);
            BuFriendListDo buFriendListDos = new BuFriendListDo();
            buFriendListDos.setUsercode(requestcode);
            buFriendListDos.setFriendcode(usercode);
            buFriendListDos.setCreatetime(new Date());
            buFriendListMapper.insert(buFriendListDos);
            return ResposeResultUtil.createResponse(ResponseCode.SUCCESS);
        }else {
            return ResposeResultUtil.createResponse(ResponseCode.FRIEND_NOT_FIND);
        }
    }

    public BuMessageDo assembleEntity(String usercode, String friendcode,String msg){
        BuMessageDo buMessageDo = new BuMessageDo();
        buMessageDo.setId(Integer.valueOf(this.createId(null)));
        buMessageDo.setReceivecode(usercode);
        buMessageDo.setRealsendcode(friendcode);
        buMessageDo.setSendcode("system");
        buMessageDo.setMessage(msg);
        buMessageDo.setMsgtype("1");
        buMessageDo.setIsreceive("N");
        return buMessageDo;
    }

    //随机生成id
    public String createId(String id){
        if(!com.alibaba.druid.util.StringUtils.isEmpty(id) || id==null){
            id ="";
        }
        Random random = new Random();
        for (int i =0;i < 6;i++){
            int r = random.nextInt(10);
            id = id+r;
        }
        //查询是否存在实体
        BuMessageDo buMessageDo = mapper.selectByPrimaryKey(id);
        if(buMessageDo!=null){
            createId(id);
        }
        return id;
    }
}
