package pactera.tf.chat.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import pactera.tf.chat.entity.SysUserDo;

@Mapper
public interface SysUserMapper {
    int deleteByPrimaryKey(String usercode);

    int insert(SysUserDo record);

    int insertSelective(SysUserDo record);

    SysUserDo selectByPrimaryKey(String usercode);

    SysUserDo selectByEmail(String email);

    SysUserDo selectByUsername(String username);

    int updateByPrimaryKeySelective(SysUserDo record);

    int updateByPrimaryKeyWithBLOBs(SysUserDo record);

    int updateByPrimaryKey(SysUserDo record);
    
    List<String> selectAllCodes();
}