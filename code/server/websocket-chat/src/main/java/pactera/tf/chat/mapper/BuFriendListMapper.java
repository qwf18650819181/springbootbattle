package pactera.tf.chat.mapper;

import pactera.tf.chat.entity.BuFriendListDo;
import pactera.tf.chat.entity.BuFriendListKey;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BuFriendListMapper {
    int deleteByPrimaryKey(BuFriendListKey key);

    int insert(BuFriendListDo record);

    int insertSelective(BuFriendListDo record);

    BuFriendListDo selectByPrimaryKey(BuFriendListKey key);

    int updateByPrimaryKeySelective(BuFriendListDo record);

    int updateByPrimaryKey(BuFriendListDo record);

    List<BuFriendListDo> selectAllFriendsByUsercode(String usercode);
}