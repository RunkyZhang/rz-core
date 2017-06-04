package com.rz.core.dao;

import com.alibaba.druid.pool.DruidDataSource;
import com.rz.core.RZHelper;
import com.rz.core.SystemException;

@SuppressWarnings("all")
public class DecryptDruidSource extends DruidDataSource {
    @Override
    public void setPassword(String password) {
        try {
            super.setPassword(RZHelper.decrypt(password));
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }
}