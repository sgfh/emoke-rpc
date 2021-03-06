package com.emoke.core.rpc.annotation;

import java.lang.annotation.*;

/**
 * @author: gfh
 * @Date: 2019/9/5 19:36
 * @Description:用来动态注入rpc接口
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface EmokeMapping {
    String requestMapping();
    Method method();
}
