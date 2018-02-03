package com.rz.core.excpetion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by renjie.zhang on 10/30/2017.
 */
public class AggregateException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private List<Throwable> causes;

    public AggregateException() {
        this.causes = new ArrayList<>();
    }

    public void initCauses(Throwable... causes) {
        if (null != causes) {
            for (Throwable cause : causes) {
                if (null != cause) {
                    this.causes.add(cause);
                }
            }
        }
    }

    public List<Throwable> getCauses() {
        return this.causes;
    }
}