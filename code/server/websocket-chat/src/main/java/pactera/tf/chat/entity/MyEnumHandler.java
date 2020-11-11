package pactera.tf.chat.entity;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import pactera.tf.chat.component.message.MessageType;

@MappedTypes(value=MessageType.class)
public class MyEnumHandler extends BaseTypeHandler<MessageType>{
	
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, MessageType parameter, JdbcType jdbcType)
			throws SQLException {
		 ps.setInt(i, parameter.getCode());
		
	}

	@Override
	public MessageType getNullableResult(ResultSet rs, String columnName) throws SQLException {
		int value = rs.getInt(columnName);
        MessageType type = null;
        if (!rs.wasNull()) {
        	 for (MessageType msgType : MessageType.values()) {
                 if(msgType.getCode()==value) {
                	 type=msgType;
                 }
             }
        }
        return type;
	}

	@Override
	public MessageType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		int value = rs.getInt(columnIndex);
        MessageType type = null;
        if (!rs.wasNull()) {
        	 for (MessageType msgType : MessageType.values()) {
                 if(msgType.getCode()==value) {
                	 type=msgType;
                 }
             }
        }
        return type;
	}

	@Override
	public MessageType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		int value = cs.getInt(columnIndex);
        MessageType type = null;
        if (!cs.wasNull()) {
        	 for (MessageType msgType : MessageType.values()) {
                 if(msgType.getCode()==value) {
                	 type=msgType;
                 }
             }
        }
        return type;
	}

}
