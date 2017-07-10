package com.rz.core.mongo;

import java.io.Serializable;

/**
 * Created by renjie.zhang on 7/10/2017.
 */
public abstract class ShardingRepositoryBase<TPo extends Serializable, TSharding> extends RepositoryBase<TPo> {
    public ShardingRepositoryBase(String connectionString, String rawTableName) {
        super(connectionString, rawTableName);
    }

    public abstract String getBaseTableName();

    public abstract String getDataBaseName();

    public abstract String BuildTableName(TSharding parameter);

    public abstract String BuildDatabaseName(TSharding parameter);
}