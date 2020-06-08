package com.emoke.core.rpc.annotation;

import java.lang.annotation.*;

/**
 * @author: gfh
 * @Date: 2019/9/5 19:36
 * @Description:用来动态注入rpc接口
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EmokeRpcService {

}
