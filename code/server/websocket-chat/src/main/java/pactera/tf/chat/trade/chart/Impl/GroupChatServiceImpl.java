package pactera.tf.chat.trade.chart.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;

import pactera.tf.chat.component.ChatTopic;
import pactera.tf.chat.component.message.MessageBo;
import pactera.tf.chat.dto.GroupDto;
import pactera.tf.chat.dto.UserDto;
import pactera.tf.chat.entity.BuGroupDo;
import pactera.tf.chat.entity.BuGroupUserKeyDo;
import pactera.tf.chat.entity.BuMessageDo;
import pactera.tf.chat.entity.SysUserDo;
import pactera.tf.chat.mapper.BuGroupMapper;
import pactera.tf.chat.mapper.BuGroupUserMapper;
import pactera.tf.chat.mapper.BuMessageMapper;
import pactera.tf.chat.mapper.SysUserMapper;
import pactera.tf.chat.redis.RedisClient;
import pactera.tf.chat.trade.KafKaSender;
import pactera.tf.chat.trade.TopicNameGenerator;
import pactera.tf.chat.trade.chart.IGroupChatService;
import pactera.tf.chat.utils.ResponseCode;
import pactera.tf.chat.utils.ResponseResult;
import pactera.tf.chat.utils.ResposeResultUtil;

import javax.annotation.Resource;
import java.util.*;

@Service
public class GroupChatServiceImpl implements IGroupChatService {

    private static final Logger logger = LoggerFactory.getLogger(GroupChatServiceImpl.class);


    @Autowired
    ChatTopic chatTopic;
    @Resource
    private BuGroupMapper BuGroupMapper;

    @Resource
    private SysUserMapper userMapper;

    @Resource
    private BuGroupUserMapper BuGroupUserMapper;

    @Autowired
    TopicNameGenerator topciGenerator;

    @Autowired
    public StringRedisTemplate redis;

    @Autowired
    RedisClient redisClient;

    @Autowired
    KafKaSender kafkaSender;

    @Resource
    private BuMessageMapper buMessageMapper;

    public static final String LOGIN_KEY="_login";

    /**
     *查询当前登录用户的群聊列表
     * @return
     */
    @Override
    public ResponseResult groupDoList(String usercode){
        logger.info("当前登录的用户:", usercode);
        List<BuGroupDo> buGroupDoList= BuGroupMapper.groupChatList(usercode);
        return ResposeResultUtil.createResponse(ResponseCode.SUCCESS,buGroupDoList);
    }

    /**
     * 创建群聊
     * @param idString
     * @param usercode
     * @param nickname
     * @return
     */
    @Override
    public ResponseResult createGroupChat(List<String> idString, String usercode, String nickname) {
        try{
            BuGroupDo buGroupDo = new BuGroupDo();
            String id =  createId(null);
            buGroupDo.setId(Integer.valueOf(id));
            buGroupDo.setCnt(idString.size());
            buGroupDo.setNickname(nickname);
            buGroupDo.setMastercode(usercode);
            BuGroupMapper.insert(buGroupDo);
            List<BuGroupUserKeyDo> list = new ArrayList<BuGroupUserKeyDo>();
            for (int i =0;i < idString.size();i++){
                BuGroupUserKeyDo BuGroupUserKeyDo = new BuGroupUserKeyDo();
                BuGroupUserKeyDo.setGroupid(Integer.valueOf(id));
                BuGroupUserKeyDo.setUsercode(idString.get(i));
                list.add(BuGroupUserKeyDo);
            }
            BuGroupUserMapper.insertList(list);
            topciGenerator.createGroupTopic(id);
            //加入 系统监听
            chatTopic.addGroupTopic(topciGenerator.getGroupTopic(id));
            return ResposeResultUtil.createResponse(ResponseCode.SUCCESS);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResposeResultUtil.createResponse(ResponseCode.ERROR);
        }
    }

    /**
     * 收集群组离线人员
     * @param groupid
     * @return
     */
    @Override
    public List<String> checkGroup(String groupid) {
       List<BuGroupUserKeyDo> buGroupUserKeyDo = BuGroupUserMapper.selectGroupByGroupId(Integer.valueOf(groupid));
       List<String> userCodeList = new ArrayList<>();
       //验证是否是离线用户，离线用户保存信息
        for (int i =0; i<buGroupUserKeyDo.size();i++){
            String userCode =buGroupUserKeyDo.get(i).getUsercode();
            if(!redis.hasKey(userCode+LOGIN_KEY)){
                userCodeList.add(userCode);
            }
        }
        return userCodeList;
    }


    //随机生成id
    public String createId(String id){
        if(!StringUtils.isEmpty(id) || id==null){
            id ="";
        }
        Random random = new Random();
        for (int i =0;i < 6;i++){
            int r = random.nextInt(10);
            id = id+r;
        }
        //查询是否存在实体
        BuGroupDo BuGroupDo = BuGroupMapper.selectByPrimaryKey(Integer.valueOf(id));
        if(BuGroupDo!=null){
            createId(id);
        }
        return id;
    }


    @Override
    public List<String> selectAllGroup(){
    	return BuGroupMapper.selectAllGroup();
    }

	@Override
	public boolean joinGroup(String groupid, String userCode) {
//		//加入群聊
        BuGroupUserKeyDo buGroupUserKeyDo = new BuGroupUserKeyDo();
        buGroupUserKeyDo.setGroupid(Integer.valueOf(groupid));
        buGroupUserKeyDo.setUsercode(userCode);

        BuGroupUserMapper.insert(buGroupUserKeyDo);
//        //发送 通知给群组
        //获取群主 
        BuGroupDo group=BuGroupMapper.selectByPrimaryKey(Integer.parseInt(groupid));
        String groupName = group.getNickname();
        String master=group.getMastercode();
        kafkaSender.sendJoinGroup(master, userCode+"加入了群聊（"+groupName+")");
        return true;
	}

	@Override
	public BuGroupDo selectGroupInfo(String groupId) {
		return BuGroupMapper.selectByPrimaryKey(Integer.parseInt(groupId));
	}

	@Override
	public List<BuGroupUserKeyDo> selectMembers(String groupId) {
		return BuGroupUserMapper.selectGroupByGroupId(Integer.parseInt(groupId));
	}

    /**
     * searchGroupInfo:群组信息查询
     * @param groupId
     * @return
     */
    @Override
    public ResponseResult searchGroupInfo(String groupId, boolean flag){

        /**
         * 1. 验证
         */
        BuGroupDo group= BuGroupMapper.selectByPrimaryKey(Integer.parseInt(groupId));
        if (group == null){
            logger.info("群组id{}不存在", groupId);
            return ResposeResultUtil.createResponse(ResponseCode.GROUP_MISS);
        }

        /**
         * 2.返回群组信息,flag为true返回成员信息
         */

        GroupDto groupDto = new GroupDto();
        groupDto.setNickName(group.getNickname());
        groupDto.setMastercode(group.getMastercode());
        groupDto.setGroupId(group.getId());
        if (!flag){
            return ResposeResultUtil.createResponse(ResponseCode.SUCCESS,groupDto);
        }

        List<BuGroupUserKeyDo> groupUsers= BuGroupUserMapper.selectGroupByGroupId(Integer.parseInt(groupId));
        List<UserDto> members = new LinkedList<UserDto>();
        for (BuGroupUserKeyDo groupUser : groupUsers){
            String usercode = groupUser.getUsercode();
            SysUserDo sysUserDo = userMapper.selectByPrimaryKey(usercode);

            if (sysUserDo == null){
                //用户不存在,临时处理
                UserDto userDto = new UserDto();
                userDto.setUserCode(usercode);
                userDto.setFlag(UserDto.OFFLINE);
                members.add(userDto);
                continue;
            }
            UserDto userDto = new UserDto(sysUserDo);
            if (redisClient.isLogin(usercode)){
                userDto.setFlag(UserDto.ONLINE);
            }
            members.add(userDto);
        }

        groupDto.setCnt(groupUsers.size());
        groupDto.setMembers(members);

        return ResposeResultUtil.createResponse(ResponseCode.SUCCESS,groupDto);
    }

}
