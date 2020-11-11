package pactera.tf.chat.mapper;

import pactera.tf.chat.entity.BuGroupDo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BuGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BuGroupDo record);

    int insertSelective(BuGroupDo record);

    BuGroupDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BuGroupDo record);

    int updateByPrimaryKey(BuGroupDo record);

    List<BuGroupDo> groupChatList(String usercode);
    
    List<String> selectAllGroup();
}