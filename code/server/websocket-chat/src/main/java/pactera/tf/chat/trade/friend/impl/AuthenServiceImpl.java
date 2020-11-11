package pactera.tf.chat.trade.friend.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import pactera.tf.chat.component.ChatTopic;
import pactera.tf.chat.component.message.MessageBo;
import pactera.tf.chat.dto.GroupDto;
import pactera.tf.chat.dto.LoginDto;
import pactera.tf.chat.dto.RegisterUserDto;
import pactera.tf.chat.dto.UserDto;
import pactera.tf.chat.entity.*;
import pactera.tf.chat.exception.BizException;
import pactera.tf.chat.mapper.*;
import pactera.tf.chat.redis.RedisClient;
import pactera.tf.chat.trade.TopicNameGenerator;
import pactera.tf.chat.trade.friend.AuthenService;
import pactera.tf.chat.utils.ResponseCode;
import pactera.tf.chat.utils.ResponseResult;
import pactera.tf.chat.utils.ResposeResultUtil;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

/**
 *
 * @author: qiuwanfeng
 * @createdate: 2020/7/10
 * @description: 用户验证相关
 *
 */
@Service
public class AuthenServiceImpl implements AuthenService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenServiceImpl.class);

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private BuFriendListMapper friendListMapper;

    @Resource
    private BuGroupUserMapper groupUserMapper;

    @Resource
    private BuGroupMapper groupMapper;
    
    @Autowired
    ChatTopic chatTopic;

    @Resource
    private BuMessageMapper buMessageMapper;

    @Autowired
    RedisClient redisClient;

    @Autowired
    TopicNameGenerator topciGenerator;

    private static final String EMAIL_REGIX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    /**
     * login
     * @param usercodeOrEmail
     * @param password
     * @return
     */
    @Override
    public LoginDto login(String usercodeOrEmail, String password) throws BizException{

        logger.info("用户登录,用户ID或邮箱{}", usercodeOrEmail);
        /**
         * 1. 使用邮箱或者用户id查询存量用户
         */
        SysUserDo sysUser;
        if (usercodeOrEmail.matches(EMAIL_REGIX)){
            sysUser = sysUserMapper.selectByEmail(usercodeOrEmail);
            if (sysUser == null){
                sysUser = sysUserMapper.selectByPrimaryKey(usercodeOrEmail);
            }
        }else {
            sysUser = sysUserMapper.selectByPrimaryKey(usercodeOrEmail);
        }
        if (sysUser == null){
            logger.info("用户登录,用户ID或邮箱{},该用户不存在", usercodeOrEmail);
            throw new BizException(ResponseCode.USER_NOT_EXISTS);
        }

        /**
         * 2.判断是否已经登录
         */
        String usercode = sysUser.getUsercode();
        if(redisClient.isLogin(usercode)) {
            logger.info("用户登录,用户ID或邮箱{},用户已经登录", usercodeOrEmail);
            throw new BizException(ResponseCode.USER_BEING_LOGIN);
        }

        /**
         * 3. 驗證密碼
         */
        if(!sysUser.getPasswd().equals(new String(decodePwd(password)).trim())) {
            logger.info("用户登录,用户ID或邮箱{},登录密码{},密码错误", usercodeOrEmail, password);
            throw new BizException(ResponseCode.WRONG_PASSWORD);
        }

        /**
         * 4. redis逻辑 系统topic 监听
         * key: userId
         * value: localIp+","+nowDate
         * expire time: 5min
         */
        redisClient.login(usercode, getLocalIp());
        chatTopic.deleteChatTopic(topciGenerator.getUserTopic(usercode));

        /**
         * 5. 更新用户状态并返回好友信息及群組信息
         */
        sysUser.setLastlogintime(new Date());
        sysUserMapper.updateByPrimaryKey(sysUser);
        logger.info("用户登录,用户ID或邮箱{},成功登录并更新状态", usercodeOrEmail);

        List<BuFriendListDo> friendList = friendListMapper.selectAllFriendsByUsercode(usercode);
        //好友信息 从 do转成dto
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

        //群组 只返回群名等大致信息，具体信息 另外调用群组信息接口
        List<BuGroupUserKeyDo> groupList = groupUserMapper.selectAllGroupByUsercode(usercode);
        //群组 从 do转成dto
        List<GroupDto> groups=new ArrayList<GroupDto>();
        for (BuGroupUserKeyDo group: groupList){
            Integer groupid = group.getGroupid();
            if (StringUtils.isEmpty(groupid)){
                continue;
            }
            BuGroupDo groupdo=groupMapper.selectByPrimaryKey(groupid);
            GroupDto dto=new GroupDto();
            dto.setNickName(groupdo.getNickname());
            dto.setGroupId(groupdo.getId());
            groups.add(dto);
        }

        /**
         * 离线消息
         */
        List<BuMessageDo> buMessageDoList = buMessageMapper.getGroupMessage(usercode);
        List<MessageBo> messages=new ArrayList<MessageBo>();
        //离线消息 从do 到bo
        for (BuMessageDo messagedo : buMessageDoList) {
			messages.add(MessageBo.coverDo2Bo(messagedo));
		}

        //组装返回值
        LoginDto loginDto=new LoginDto();
        loginDto.setNickName(sysUser.getNick());
        loginDto.setUserCode(sysUser.getUsercode());
        loginDto.setFriends(friends);
        loginDto.setGroupds(groups);
        loginDto.setOfflineMessages(messages);
        return loginDto;
    }

    /**
     * 密码加密
     * @return
     */
    private byte[] decodePwd(String pwd){
        return DigestUtils.md5Digest(pwd.getBytes());
    }

    /**
     * logout
     * @param usercode
     * @return
     */
    @Override
    public ResponseResult logout(String usercode) {

        logger.info("用户登出,用户ID{}", usercode);
        /**
         * 1. 刪除redis信息
         */
        redisClient.logout(usercode);

        /**
         * 2.更新个人离线信息
         */
        buMessageMapper.updateMessageByUserCode(usercode);
        chatTopic.addChatTopic(topciGenerator.getUserTopic(usercode));

        return ResposeResultUtil.createResponse(ResponseCode.SUCCESS);
    }

    /**
     * checkUser
     * @param usercode
     * @return
     */
    @Override
    public ResponseResult checkUser(String usercode){
        SysUserDo user = sysUserMapper.selectByPrimaryKey(usercode);
        if (user != null){
            logger.info("用户验证,用户ID{},该用户已存在", usercode);
            return ResposeResultUtil.createResponse(ResponseCode.USER_EXISTS);
        }
        logger.info("用户验证,用户ID{},该用户未被创建", usercode);
        return ResposeResultUtil.createResponse(ResponseCode.SUCCESS);
    }

    /**
     * checkEmail
     * @param email
     * @return
     */
    @Override
    public ResponseResult checkEmail(String email){
        SysUserDo selectUser = new SysUserDo();
        selectUser.setEmail(email);
        SysUserDo user = sysUserMapper.selectByEmail(email);
        if (user != null){
            logger.info("邮箱验证,邮箱{},该邮箱已被注册", email);
            return ResposeResultUtil.createResponse(ResponseCode.EMAIL_USED);
        }
        logger.info("邮箱验证,邮箱{},该邮箱未被注册", email);
        return ResposeResultUtil.createResponse(ResponseCode.SUCCESS);
    }

    /**
     * register
     * @param registerUserDto
     * @return
     */
    @Override
    public ResponseResult register(RegisterUserDto registerUserDto){
        SysUserDo sysUser = new SysUserDo(registerUserDto);
        int affectRows = sysUserMapper.insertSelective(sysUser);
        if (affectRows != 1){
            return ResposeResultUtil.createResponse(ResponseCode.ERROR);
        }
        logger.info("用户注册,注册用户信息:{}", sysUser.toString());
        //创建注册用户的 topic
        topciGenerator.createUserTopic(sysUser.getUsercode());
        
        //加入系统监听 
        chatTopic.addChatTopic(topciGenerator.getUserTopic(sysUser.getUsercode()));
        return ResposeResultUtil.createResponse(ResponseCode.SUCCESS);//TODO: 返回用户id
    }

    /**
     * resetPwd
     * @param usercode
     * @param priPassword
     * @param newPassword
     * @return
     */
    @Override
    public ResponseResult resetPwd(String usercode, String priPassword, String newPassword){

        /**
         * 1. 验证原密码
         */
        SysUserDo sysUser = sysUserMapper.selectByPrimaryKey(usercode);
        priPassword = new String(decodePwd(priPassword));
        if (! priPassword.equals(sysUser.getPasswd())){
            logger.info("用户{}重置密码,原密码输入错误", usercode);
            return ResposeResultUtil.createResponse(ResponseCode.WRONG_PASSWORD);
        }

        /**
         * 2. 更新用户密码
         */
        sysUser.setPasswd(new String(decodePwd(newPassword)));
        sysUserMapper.updateByPrimaryKey(sysUser);
        logger.info("用户{}重置密码成功", usercode);
        return ResposeResultUtil.createResponse(ResponseCode.SUCCESS);
    }

    /**
     * forgetPwd
     * @param usercode
     * @param email
     * @param newPassword
     * @return
     */
    @Override
    public ResponseResult forgetPwd(String usercode, String email, String newPassword){

        /**
         * 1. 验证用户名
         */
        SysUserDo sysUser = sysUserMapper.selectByPrimaryKey(usercode);
        if (sysUser == null){
            logger.info("用户忘记密码,该用户{}不存在", usercode);
            return ResposeResultUtil.createResponse(ResponseCode.USER_NOT_EXISTS);
        }else if (! email.equals(sysUser.getEmail())){
            logger.info("用户忘记密码,该用户{}邮箱{}验证失败", usercode,email);
            return ResposeResultUtil.createResponse(ResponseCode.WRONG_EMAIL);
        }

        /**
         * 2. 更新用户密码
         */
        sysUser.setPasswd(new String(decodePwd(newPassword)));
        sysUserMapper.updateByPrimaryKey(sysUser);
        return ResposeResultUtil.createResponse(ResponseCode.SUCCESS);
    }

    /**
     * heartbeat:心跳包
     * @param usercode
     * @return
     */
    @Override
    public ResponseResult heartbeat(String usercode){

        /**
         * 1. 先判断用户是否已经在线
         */
        if (!redisClient.isLogin(usercode)){
            return ResposeResultUtil.createResponse(ResponseCode.NEED_LOGIN);
        }

        /**
         * 2. 在线就更新redis，返回好友列表最新状态
         */
        redisClient.expire(usercode);

        List<BuFriendListDo> friendList = friendListMapper.selectAllFriendsByUsercode(usercode);
        //好友信息 从 do转成dto
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

    /**
     * 获取本地ip地址
     * @return
     */
    private String getLocalIp(){
        StringBuilder sb = new StringBuilder();
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            //获取本地所有网络接口
            while (en.hasMoreElements()) {
                //遍历枚举中的每一个元素
                NetworkInterface ni= (NetworkInterface) en.nextElement();
                Enumeration<InetAddress> enumInetAddr = ni.getInetAddresses();
                while (enumInetAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumInetAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()  && !inetAddress.isLinkLocalAddress()
                            && inetAddress.isSiteLocalAddress()) {
                        sb.append(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            logger.error(e.getMessage());
        }
        return sb.toString();
    }

    @Override
    public List<String> selectAllUser(){
    	return sysUserMapper.selectAllCodes();
    }
    
    
}
