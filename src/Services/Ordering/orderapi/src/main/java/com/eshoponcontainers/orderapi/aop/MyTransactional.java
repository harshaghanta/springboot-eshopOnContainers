package com.eshoponcontainers.orderapi.aop;

import org.springframework.transaction.annotation.Transactional;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Transactional
public @interface MyTransactional {
    // You can add custom attributes here if needed
}
