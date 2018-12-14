package com.lehow.net.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * desc:
 * author: luoh17
 * time: 2018/12/6 14:55
 */
@Retention(CLASS) @Target(METHOD) public @interface MockNet {
}
