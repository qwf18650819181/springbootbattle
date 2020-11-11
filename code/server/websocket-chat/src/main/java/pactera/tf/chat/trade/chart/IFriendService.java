package pactera.tf.chat.trade.chart;


import pactera.tf.chat.entity.BuFriendListDo;
import pactera.tf.chat.utils.ResponseResult;



public interface IFriendService {

    /**
     * 好友列表
     * @param usercode
     * @return
     */
    ResponseResult friendList(String usercode);


    /**
     * 查询是否存在该好友
     * @param userCode
     * @param friendcode
     * @return
     */
    BuFriendListDo findFrindByFriendcode(String userCode,String friendcode);


    /**
     * 添加好友
     * @param usercode
     * @param friendcode
     * @return
     */
    ResponseResult addFriend(String usercode , String friendcode,String msg, String redisKey);


    /**
     * 同意好友请求
     * @param usercode
     * @return
     */
    ResponseResult agreeRequest(String usercode,String requestcode,String msg, String redisKey);

}
