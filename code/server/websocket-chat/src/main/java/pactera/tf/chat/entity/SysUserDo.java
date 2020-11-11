package pactera.tf.chat.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.DigestUtils;
import pactera.tf.chat.dto.RegisterUserDto;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@ApiModel(description = "用户")
public class SysUserDo {

    @ApiModelProperty(value = "用户id")
    private String usercode;

    @ApiModelProperty(value = "密码")
    private String passwd;

    @ApiModelProperty(value = "姓名")
    private String username;

    @ApiModelProperty(value = "昵称")
    private String nick;

    @ApiModelProperty(value = "创建时间")
    private Date createtime;

    @ApiModelProperty(value = "最近登录时间")
    private Date lastlogintime;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "头像")
    private byte[] photo;

    public SysUserDo() {
    }

    public SysUserDo(RegisterUserDto registerUserDto) {
        this.passwd = new String(decodePwd(registerUserDto.getPasswd()));
        this.username = registerUserDto.getUserName();
        this.nick = registerUserDto.getNick();
        this.email = registerUserDto.getEmail();
        this.createtime = new Date();
        this.lastlogintime = this.createtime;
        this.usercode = "WSC"+this.createtime.getTime();
    }

    /**
     * 密码加密
     * @return
     */
    private byte[] decodePwd(String pwd){
        return DigestUtils.md5Digest(pwd.getBytes());
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode == null ? null : usercode.trim();
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd == null ? null : passwd.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick == null ? null : nick.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getLastlogintime() {
        return lastlogintime;
    }

    public void setLastlogintime(Date lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "SysUserDo{" +
                "usercode='" + usercode + '\'' +
                ", passwd='" + passwd + '\'' +
                ", username='" + username + '\'' +
                ", nick='" + nick + '\'' +
                ", createtime=" + createtime +
                ", lastlogintime=" + lastlogintime +
                ", email='" + email + '\'' +
                ", photo=" + Arrays.toString(photo) +
                '}';
    }
}