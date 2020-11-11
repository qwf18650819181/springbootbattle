package pactera.tf.chat.trade.friend;

import pactera.tf.chat.dto.LoginDto;
import pactera.tf.chat.dto.RegisterUserDto;
import pactera.tf.chat.exception.BizException;
import pactera.tf.chat.utils.ResponseResult;

import java.util.List;

/**
 *
 * @author: qiuwanfeng
 * @createdate: 2020/7/10
 * @description: 用户验证相关
 *
 */
public interface AuthenService {

    /**
     *用户登录
     */
    LoginDto login(String usercodeOrEmail, String password) throws BizException;

    /**
     *用户登出
     */
    ResponseResult logout(String usercode);

    /**
     *验证用户
     */
    ResponseResult checkUser(String usercode);

    /**
     *邮箱验证
     */
    ResponseResult checkEmail(String email);

    /**
     *用户注册
     */
    ResponseResult register(RegisterUserDto registerUserDto);

    /**
     *重置密码
     */
    ResponseResult resetPwd(String usercode, String priPassword, String newPassword);

    /**
     *忘记密码
     */
    ResponseResult forgetPwd(String usercode, String email, String newPassword);

    /**
     *心跳检测
     */
    ResponseResult heartbeat(String usercode);

    List<String> selectAllUser();
}
