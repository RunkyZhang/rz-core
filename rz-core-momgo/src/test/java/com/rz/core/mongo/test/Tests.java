package com.rz.core.mongo.test;

import com.rz.core.mongo.RepositoryBase;

/**
 * Created by renjie.zhang on 7/10/2017.
 */
public class Tests {
    public static void main(String[] args) {
        Tests tests = new Tests();

        try {
            tests.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void test() {
        PoBase poBase = new PoBase();
        poBase.setId(1111);

        //System.out.println(RepositoryBase.getIdValue(poBase));
    }
}
