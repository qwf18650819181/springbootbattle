package pactera.tf.chat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import pactera.tf.chat.entity.BuMessageDo;

@Mapper
public interface BuMessageMapper {
    int deleteByPrimaryKey(String id);

    int insert(BuMessageDo record);

    int insertSelective(BuMessageDo record);

    BuMessageDo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BuMessageDo record);

    int updateByPrimaryKey(BuMessageDo record);
    
    List<BuMessageDo> getAllOffLineUser();

    /**
     * 批量插入
     * @param list
     */
    void insertMessage(List<BuMessageDo> list);


    /**
     * 获取用户离线的群聊信息
     * @param userCode
     * @return
     */
    List<BuMessageDo> getGroupMessage(String userCode);


    /**
     * 更新个人离线消息状态
     */
    void updateMessageByUserCode(String userCode);

}