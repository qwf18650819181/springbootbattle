package pactera.tf.chat.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pactera.tf.chat.dto.LoginDto;
import pactera.tf.chat.dto.RegisterUserDto;
import pactera.tf.chat.trade.friend.AuthenService;
import pactera.tf.chat.utils.ResponseCode;
import pactera.tf.chat.utils.ResponseResult;
import pactera.tf.chat.utils.ResposeResultUtil;

/**
 *
 * @author: qiuwanfeng
 * @createdate: 2020/7/10
 * @description: 用户验证相关
 *
 */
@Api(tags = "用户验证")
@RestController
@RequestMapping("/v")
public class AuthenController {

    @Autowired
    private AuthenService authenService;

    @ApiOperation(value = "用户登录", notes = "登录逻辑")
    @PostMapping(value = "/user/login")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "usercodeOrEmail", value = "用户id或邮箱", required = false),
            @ApiImplicitParam(name = "password", value = "用户密码", required = false)
    })
    public ResponseResult login(@RequestParam String usercodeOrEmail, @RequestParam String password) {
        LoginDto dto=authenService.login(usercodeOrEmail, password);
        return ResposeResultUtil.success(dto);
    }

    @ApiOperation(value = "用户登出", notes = "登出逻辑")
    @PostMapping(value = "/logout")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "usercode", value = "用户id", required = true),
    })
    public ResponseResult logout(@RequestParam String usercode) {
        return authenService.logout(usercode);
    }

    @ApiOperation(value = "用户是否存在", notes = "验证逻辑")
    @PostMapping(value = "/checkUser")
    @ResponseBody
    @ApiImplicitParam(name = "usercode", value = "用户id", required = true)
    public ResponseResult checkUser(@RequestParam String usercode) {
        return authenService.checkUser(usercode);
    }

    @ApiOperation(value = "邮箱是否使用", notes = "验证逻辑")
    @PostMapping(value = "/checkEmail")
    @ResponseBody
    @ApiImplicitParam(name = "email", value = "邮箱", required = true)
    public ResponseResult checkEmail(@RequestParam String email) {
        return authenService.checkEmail(email);
    }

    @ApiOperation(value = "用户注册", notes = "注册逻辑")
    @PostMapping(value = "/register")
    @ResponseBody
    public ResponseResult register(RegisterUserDto registerUserDto) {
        return authenService.register(registerUserDto);
    }

    @ApiOperation(value = "用户改密", notes = "改密逻辑")
    @PostMapping(value = "/resetPwd")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "usercode", value = "用户id", required = true),
            @ApiImplicitParam(name = "priPassword", value = "原用户密码", required = true),
            @ApiImplicitParam(name = "newPassword", value = "新用户密码", required = true)
    })
    public ResponseResult resetPwd(@RequestParam String usercode, @RequestParam String priPassword, @RequestParam String newPassword) {
        return authenService.resetPwd(usercode, priPassword, newPassword);
    }

    @ApiOperation(value = "忘记密码", notes = "改密逻辑")
    @PostMapping(value = "/forgetPwd")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "usercode", value = "用户id", required = true),
            @ApiImplicitParam(name = "email", value = "邮箱", required = true),
            @ApiImplicitParam(name = "newPassword", value = "新用户密码", required = true)
    })
    public ResponseResult forgetPwd(@RequestParam String usercode, @RequestParam String email, @RequestParam String newPassword) {
        return authenService.forgetPwd(usercode, email, newPassword);
    }

    @ApiOperation(value = "心跳检测", notes = "心跳服务端逻辑")
    @ResponseBody
    @PostMapping(value="/heartbeat/{usercode}")
    @ApiImplicitParam(name = "usercode", value = "用户id", required = true)
    public ResponseResult heartbeat(@PathVariable("usercode") String usercode) {
        if (StringUtils.isEmpty(usercode)){
            return ResposeResultUtil.createResponse(ResponseCode.ILLEGAL_ARGUMENT);
        }
        return authenService.heartbeat(usercode);
    }


}
