package pactera.tf.chat.trade.chart;

import pactera.tf.chat.entity.BuGroupDo;
import pactera.tf.chat.utils.ResponseResult;
import pactera.tf.chat.utils.ResposeResultUtil;

import java.util.List;

public interface ThumbUpChatService {


    /**
     * 好友点赞
     * @param usercode
     * @param thumbUpId
     * @return
     */
    ResponseResult thumbUpChat(String usercode,String thumbUpId);

}
