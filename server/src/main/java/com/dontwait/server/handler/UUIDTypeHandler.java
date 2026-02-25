package com.dontwait.server.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(UUID.class)
@MappedJdbcTypes(JdbcType.OTHER)
public class UUIDTypeHandler extends BaseTypeHandler<java.util.UUID> {

    @Override
    public UUID getNullableResult(ResultSet arg0, String arg1)
            throws SQLException {
        String vStr = arg0.getString(arg1);
        return getValue(vStr);
    }

    @Override
    public UUID getNullableResult(ResultSet arg0, int arg1) throws SQLException {
        String vStr = arg0.getString(arg1);
        return getValue(vStr);
    }

    @Override
    public UUID getNullableResult(CallableStatement arg0, int arg1)
            throws SQLException {
        String vStr = arg0.getString(arg1);
        return getValue(vStr);
    }

    @Override
    public void setNonNullParameter(PreparedStatement arg0, int arg1,
            UUID arg2, JdbcType arg3) throws SQLException {
        if (null != arg2) {
            arg0.setObject(arg1, arg2);
        }
    }

    private UUID getValue(String vStr) {
        if (notNull(vStr)) {
            return UUID.fromString(vStr);
        }
        return null;
    }

    private boolean notNull(String arg1) {
        return (null != arg1 && !"".equals(arg1));
    }
}
