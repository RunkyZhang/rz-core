package com.rz.core.dao.aop;

import com.rz.core.dao.access.DataOperationTypeEnum;
import com.rz.core.dao.access.DataOperationTypeHolder;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by renjie.zhang on 2/1/2018.
 */
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class DaoInterceptor implements Interceptor {
    private static final String WRITE_SQL = ".*insert.*|.*delete.*|.*update.*";
    private Map<String, DataOperationTypeEnum> mapperDataOperationTypes = new ConcurrentHashMap<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // synchronizationActive ???
        boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        if (!synchronizationActive) {
            Object[] objects = invocation.getArgs();
            MappedStatement mappedStatement = (MappedStatement) objects[0];

            if (!this.mapperDataOperationTypes.containsKey(mappedStatement.getId())) {
                DataOperationTypeEnum dataOperationType;

                if (mappedStatement.getSqlCommandType().equals(SqlCommandType.SELECT)) {
                    if (mappedStatement.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                        dataOperationType = DataOperationTypeEnum.WRITE;
                    } else {
                        BoundSql boundSql = mappedStatement.getSqlSource().getBoundSql(objects[1]);
                        String sql = boundSql.getSql().toLowerCase().replaceAll("[\\t\\n\\r]", " ");
                        if (sql.matches(DaoInterceptor.WRITE_SQL)) {
                            dataOperationType = DataOperationTypeEnum.WRITE;
                        } else {
                            dataOperationType = DataOperationTypeEnum.READ;
                        }
                    }
                } else {
                    dataOperationType = DataOperationTypeEnum.WRITE;
                }

                this.mapperDataOperationTypes.put(mappedStatement.getId(), dataOperationType);
            }
            DataOperationTypeHolder.set(this.mapperDataOperationTypes.get(mappedStatement.getId()));
        }

        System.out.println(invocation.getMethod().getName());
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        System.out.println(properties);
    }
}

