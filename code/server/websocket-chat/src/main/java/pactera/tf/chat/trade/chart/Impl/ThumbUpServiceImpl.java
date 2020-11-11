package pactera.tf.chat.trade.chart.Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import pactera.tf.chat.entity.BuMessageDo;
import pactera.tf.chat.entity.SysUserDo;
import pactera.tf.chat.mapper.BuMessageMapper;
import pactera.tf.chat.mapper.SysUserMapper;
import pactera.tf.chat.trade.KafKaSender;
import pactera.tf.chat.trade.TopicNameGenerator;
import pactera.tf.chat.trade.chart.IGroupChatService;
import pactera.tf.chat.trade.chart.ThumbUpChatService;
import pactera.tf.chat.utils.ResponseCode;
import pactera.tf.chat.utils.ResponseResult;
import pactera.tf.chat.utils.ResposeResultUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ThumbUpServiceImpl implements ThumbUpChatService {

    private static final Logger logger = LoggerFactory.getLogger(ThumbUpServiceImpl.class);

    @Resource
    private SysUserMapper sysUserMapper;
    
    @Resource
    private BuMessageMapper buMessageMapper;


    @Autowired
    TopicNameGenerator topciGenerator;

    @Autowired
    KafKaSender kafKaSender;
    
    @Resource(name="stringRedisTemplate")
	private RedisTemplate<String,String> redisTemplate;

//    //随机生成id
//    public String createId(String id){
//        if(id!=null){
//            id =null;
//        }
//        Random random = new Random();
//        for (int i =0;i < 6;i++){
//            int r = random.nextInt(10);
//            id = id+r;
//        }
//        //查询是否存在实体
//        BuMessageDo BuMessageDo = buMessageMapper.selectByPrimaryKey(id);
//        if(BuMessageDo.getId()!=null){
//            createId(id);
//        }
//        return id;
//    }
    
    /**
     * 好友点赞
     * @param usercode
     * @param thumbUpId
     * @return
     */
	@Override
	public ResponseResult thumbUpChat(String usercode, String thumbUpId) {
		try{
			String redisKey=thumbUpId+"_GOOD";
			//点赞先加redis，然后发消息通知
			if(!redisTemplate.hasKey(redisKey)) {
				redisTemplate.opsForValue().set(redisKey, "1");
			}
			redisTemplate.opsForValue().increment(redisKey);
			SysUserDo user = sysUserMapper.selectByPrimaryKey(usercode);
			kafKaSender.sendThumbUp(usercode,thumbUpId,user.getUsername());
            return ResposeResultUtil.createResponse(ResponseCode.SUCCESS);
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResposeResultUtil.createResponse(ResponseCode.ERROR);
        }
	}




}
