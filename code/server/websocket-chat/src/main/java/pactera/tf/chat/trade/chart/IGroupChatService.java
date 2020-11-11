package pactera.tf.chat.trade.chart;

import pactera.tf.chat.component.message.MessageBo;
import pactera.tf.chat.entity.BuGroupDo;
import pactera.tf.chat.entity.BuGroupUserKeyDo;
import pactera.tf.chat.entity.BuMessageDo;
import pactera.tf.chat.utils.ResponseResult;
import pactera.tf.chat.utils.ResposeResultUtil;

import java.util.List;

public interface IGroupChatService {

    /**
     * 查询当前登录用户的群聊列表
     * @param usercode
     * @return
     */
    ResponseResult groupDoList(String usercode);

    /**
     * 创建群聊
     * @param idString
     * @param usercode
     * @param nickname
     * @return
     */
    ResponseResult createGroupChat(List<String> idString,String usercode,String nickname);


    /**
     * 收集群组离线人员
     * @param groupid
     * @return
     */
    List<String> checkGroup(String groupid);

    /**
     * 获取所有的群组
     * @return
     */
	List<String> selectAllGroup();

	/**
	 * 加入群组
	 * @param groupid
	 * @param userCode
	 * @return
	 */
	boolean joinGroup(String groupid,String userCode);
	
	/**
	 * 群组信息
	 * @param groupId
	 * @return
	 */
	BuGroupDo selectGroupInfo(String groupId);
	
	/**
	 * 群聊人员信息
	 * @param groupId
	 * @return
	 */
	List<BuGroupUserKeyDo> selectMembers(String groupId);

	/**
	 *群组信息查询
	 */
	ResponseResult searchGroupInfo(String groupId, boolean flag);

}
