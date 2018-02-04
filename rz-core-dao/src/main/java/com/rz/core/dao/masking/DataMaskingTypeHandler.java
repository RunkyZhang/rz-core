package com.rz.core.dao.masking;

import com.rz.core.dao.SourcePool;
import com.rz.core.dao.excpetion.DaoException;
import com.rz.core.dao.masking.DataMasker;
import com.rz.core.dao.model.ModelDefinition;
import com.rz.core.dao.model.ModelFieldDefinition;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.StringTypeHandler;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by renjie.zhang on 1/29/2018.
 */
@MappedJdbcTypes(value = {JdbcType.VARCHAR, JdbcType.CHAR, JdbcType.NVARCHAR, JdbcType.NCHAR}, includeNullJdbcType = true)
@MappedTypes({String.class})
public class DataMaskingTypeHandler extends StringTypeHandler {
    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String tableName = rs.getMetaData().getTableName(1);
        if (!SourcePool.modelDefinitions.containsKey(tableName)) {
            return rs.getString(columnName);
        }
        ModelDefinition poDefinition = SourcePool.modelDefinitions.get(tableName);
        if (!poDefinition.containsPoFieldDefinition(columnName)) {
            return rs.getString(columnName);
        }
        ModelFieldDefinition poFieldDefinition = poDefinition.getPoFieldDefinition(columnName);
        if (!poFieldDefinition.isDataMasking()) {
            return rs.getString(columnName);
        }

        String value = rs.getString(columnName);
        return StringUtils.isBlank(value) ? value : value.substring(value.length() / 2);
    }

    public static void setParameter(String tableName, String fieldName, Object parameter) {
        if (!SourcePool.modelDefinitions.containsKey(tableName)) {
            return;
        }
        ModelDefinition poDefinition = SourcePool.modelDefinitions.get(tableName);
        if (!poDefinition.containsPoFieldDefinition(fieldName)) {
            return;
        }
        ModelFieldDefinition modelFieldDefinition = poDefinition.getPoFieldDefinition(fieldName);
        if (!modelFieldDefinition.isDataMasking()) {
            return;
        }

        try {
            Object rawValue = modelFieldDefinition.getValue(parameter);
            String value = null == rawValue ? null : String.valueOf(rawValue);
            modelFieldDefinition.setValue(parameter, value + value);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new DaoException(e);
        }
    }

    public static void registerPoDefinition(ModelDefinition modelDefinition) {
        if (null != modelDefinition) {
            SourcePool.modelDefinitions.put(modelDefinition.getTableName(), modelDefinition);
        }
    }

    public static void registerDataMasker(String id, DataMasker dataMasker) {
        if (null != dataMasker) {
            SourcePool.dataMaskers.put(id, dataMasker);
        }
    }
}
