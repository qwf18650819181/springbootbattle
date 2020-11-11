package pactera.tf.chat.mapper;

import pactera.tf.chat.entity.BuGroupUserKeyDo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BuGroupUserMapper {
    int deleteByPrimaryKey(BuGroupUserKeyDo key);

    int insert(BuGroupUserKeyDo record);

    int insertSelective(BuGroupUserKeyDo record);

    void insertList(List<BuGroupUserKeyDo> list);

    List<BuGroupUserKeyDo> selectAllGroupByUsercode(String usercode);

    List<BuGroupUserKeyDo> selectGroupByGroupId(Integer groupId);
}