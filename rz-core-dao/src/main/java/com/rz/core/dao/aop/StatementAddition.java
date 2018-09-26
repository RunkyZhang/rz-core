package com.rz.core.dao.aop;

import com.rz.core.Assert;
import com.rz.core.dao.access.DataOperationTypeEnum;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.Statements;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by renjie.zhang on 2/5/2018.
 */
class StatementAddition {
    private String statementId;
    private Statements statements;
    private Set<String> tableNames;
    private String sql;
    private DataOperationTypeEnum dataOperationType;

    public String getStatementId() {
        return statementId;
    }

    public String getSql() {
        return sql;
    }

    public Statements getStatements() {
        return statements;
    }

    public DataOperationTypeEnum getDataOperationType() {
        return dataOperationType;
    }

    public Set<String> getTableNames() {
        return tableNames;
    }

    public StatementAddition(MappedStatement mappedStatement, Object parameter) throws JSQLParserException {
        Assert.isNotNull(mappedStatement, "mappedStatement");

        this.statementId = mappedStatement.getId();
        BoundSql boundSql = mappedStatement.getSqlSource().getBoundSql(parameter);
        this.sql = boundSql.getSql();
        this.tableNames = new HashSet<>();
        this.statements = CCJSqlParserUtil.parseStatements(this.sql);
        this.dataOperationType = DataOperationTypeEnum.READ;

        for (Statement statement : statements.getStatements()) {
            if (!(statement instanceof Select)) {
                this.dataOperationType = DataOperationTypeEnum.WRITE;
            }

            TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
            List<String> partTableNames = tablesNamesFinder.getTableList(statement);
            if (null != partTableNames) {
                for (String tableName : partTableNames) {
                    if (null == tableName) {
                        continue;
                    }
                    this.tableNames.add(tableName.toLowerCase());
                }
            }
        }

        if (SqlCommandType.SELECT == mappedStatement.getSqlCommandType()) {
            if (statementId.contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
                this.dataOperationType = DataOperationTypeEnum.WRITE;
            }
        } else {
            dataOperationType = DataOperationTypeEnum.WRITE;
        }
    }
}
