package com.rz.core;

/**
 * Created by renjie.zhang on 7/14/2017.
 */
public class Tuple3<T1, T2, T3> extends Tuple2<T1, T2> {
    private T3 item3;

    public T3 getItem3() {
        return item3;
    }

    public void setItem3(T3 item3) {
        this.item3 = item3;
    }

    public Tuple3(T1 item1, T2 item2, T3 item3) {
        super(item1, item2);

        this.item3 = item3;
    }
}