package pactera.tf.chat.utils;

import pactera.tf.chat.exception.BaseErrorInfoInterface;

/**
 * 响应状态码
 */
public enum ResponseCode implements BaseErrorInfoInterface{

    // 系统模块
    SUCCESS("0", "操作成功"),
    ERROR("1", "操作失败"),
    SERVER_ERROR("500", "服务器异常"),
    BODY_NOT_MATCH("400","请求的数据格式不符!"),

    // 通用模块
    ILLEGAL_ARGUMENT("10000", "参数不合法"),
    REPETITIVE_OPERATION("10001", "请勿重复操作"),
    ACCESS_LIMIT("10002", "请求太频繁, 请稍后再试"),
    MAIL_SEND_SUCCESS("10003", "邮件发送成功"),

    // 用户模块
    NEED_LOGIN("20000", "登录失效"),
    USER_BEING_LOGIN("20001","用户已登录"),
    USERNAME_OR_PASSWORD_EMPTY("20002", "用户名或密码不能为空"),
    USERNAME_OR_PASSWORD_WRONG("20003", "用户名或密码错误"),
    USER_NOT_EXISTS("20004", "用户不存在"),
    USER_EXISTS("20005", "用户已存在"),
    WRONG_PASSWORD("20006", "密码错误"),
    EMAIL_USED("20007", "邮箱已被使用"),
    WRONG_EMAIL("20008", "邮箱不匹配"),
    EMAIL_EMPTY("20009", "邮箱不能为空"),
    NICK_EMPTY("20010", "昵称不能为空"),
    USERNAME_UNCORRECT("20011", "用户名不合法"),
    USER_REGISTERSUCCESS("20012","用户注册成功"),

    //好友模块
    FRIEND_ALLREADY_ADD("30001","好友已存在"),
    
    //群组模块
    GROUP_MAST_TOW("40001","群组必须有两位以上的成员"),
	GROUP_MISS("40002","不存在的群组"),
	GROUP_JOIN_FAIL("40003","加入群组失败"),




    FRIEND_JOIN_FALL("50001","添加用户失败,已添加该用户"),
    FRIEND_JOIN_WAIT("50002","添加用户失败,正在等待对方好友申请通过"),
    FRIEND_INEXISTENCE("50003","添加用户失败,该用户不存在"),
    FRIEND_NOT_FIND("50004","请求失败,查找不到相关信息"),
    ;
    ResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
