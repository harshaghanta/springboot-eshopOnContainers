package com.eshoponcontainers.orderapi.aop;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Transactional(propagation = Propagation.REQUIRES_NEW)
public @interface MyTransactional {
    Propagation propagation() default Propagation.REQUIRES_NEW;
    // You can add custom attributes here if needed
}
