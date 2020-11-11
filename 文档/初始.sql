CREATE TABLE SYS_USER(
usercode VARCHAR(20) PRIMARY KEY ,
passwd VARCHAR(100) NOT NULL,
username VARCHAR(32) NOT NULL,
nick  VARCHAR(100) NOT NULL,
createtime TIMESTAMP NOT NULL,
lastlogintime TIMESTAMP NOT NULL,
photo BLOB ,
email VARCHAR(100)
);
ALTER TABLE `chat`.`sys_user`   
  CHANGE `usercode` `usercode` VARCHAR(20) CHARSET utf8 COLLATE utf8_general_ci NOT NULL  COMMENT '用户id',
  CHANGE `passwd` `passwd` VARCHAR(100) CHARSET utf8 COLLATE utf8_general_ci NOT NULL  COMMENT '密码',
  CHANGE `username` `username` VARCHAR(32) CHARSET utf8 COLLATE utf8_general_ci NOT NULL  COMMENT '姓名',
  CHANGE `nick` `nick` VARCHAR(100) CHARSET utf8 COLLATE utf8_general_ci NOT NULL  COMMENT '昵称',
  CHANGE `createtime` `createtime` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  NOT NULL  COMMENT '创建时间',
  CHANGE `lastlogintime` `lastlogintime` TIMESTAMP DEFAULT '0000-00-00 00:00:00'   NOT NULL  COMMENT '最后登录时间',
  CHANGE `photo` `photo` BLOB NULL  COMMENT '头像',
  CHANGE `email` `email` VARCHAR(100) CHARSET utf8 COLLATE utf8_general_ci NULL  COMMENT '邮箱';



CREATE TABLE BU_GROUP(
id INT PRIMARY KEY COMMENT '群组id',
nickname VARCHAR(50) NOT NULL COMMENT '群组名称',
cnt  INT NOT NULL COMMENT '群成员', 
mastercode VARCHAR(30) COMMENT '群主的id'
);

CREATE TABLE BU_MESSAGE(
id VARCHAR(30) PRIMARY KEY COMMENT '消息id',
receivecode VARCHAR(30) NOT NULL COMMENT '接收用户code',
realsendcode VARCHAR(30) COMMENT '实际发送用户code',
sendcode VARCHAR(30) NOT NULL COMMENT '发送用户code',
message VARCHAR(2000) NOT NULL COMMENT '消息内容',
msgtype VARCHAR(1)  NOT NULL COMMENT '消息类型(0.聊天消息1.加好友2.点赞3.群组消息)',
isreceive VARCHAR(1) NOT NULL COMMENT '是否已经获取 Y,N'
);

CREATE TABLE BU_GROUP_USER(
groupid INT COMMENT '群组id',
usercode VARCHAR(30) COMMENT '用户id',
PRIMARY KEY (`groupid`,`usercode`) 
);

CREATE TABLE BU_FRIEND_LIST(
usercode VARCHAR(30) COMMENT '用户id',
friendcode VARCHAR(30) COMMENT '好友id',
createtime TIMESTAMP  COMMENT '加好友时间',
PRIMARY KEY (`usercode`,`friendcode`)
);
