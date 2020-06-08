package com.emoke.core.rpc.proxy;


import java.lang.reflect.Proxy;

public class EmokeProxy {
    public static Object createProxy(Class<?> cls) {
        return Proxy.newProxyInstance(
                cls.getClassLoader(),
                new Class[]{cls},
                new EmokeInvocationHandler());
    }

    public static void main(String[] args) {
        Test test = (Test) EmokeProxy.createProxy(Test.class);
        System.out.println(test.getClass());
        test.say();
    }
}