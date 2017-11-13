package com.example;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Target(FIELD)
@Retention(SOURCE)
public @interface BindView {
    /**
     * 用来装id
     *
     * @return
     */
    int value();
}