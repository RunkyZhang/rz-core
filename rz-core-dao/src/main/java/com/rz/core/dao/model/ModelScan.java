package com.rz.core.dao.model;

/**
 * Created by renjie.zhang on 2/2/2018.
 */

import com.rz.core.dao.config.DaoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import({ModelScannerRegistrar.class, DaoConfig.class})
public @interface ModelScan {
    String[] value() default {};
}
