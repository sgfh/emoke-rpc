package com.emoke.core.rpc.proxy;

import com.emoke.core.rpc.annotation.EmokeMapping;
import com.emoke.core.rpc.annotation.EmokeRpcClient;
import com.emoke.core.rpc.exception.RpcException;
import com.emoke.core.rpc.http.HttpUtil;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;

public class EmokeInvocationHandler implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //已实现的具体类
        if (Object.class.equals(method.getDeclaringClass())) {
            try {
                return method.invoke(this, args);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            //接口
        } else {
            return run(method, args);
        }
        return null;
    }

    /**
     * 实现接口的核心方法
     *
     * @param method:方法
     * @param args:参数
     * @return Object
     */
    public Object run(Method method, Object[] args) {

        Class<?> declaringClass = method.getDeclaringClass();
        EmokeRpcClient emokeRpcClient = declaringClass.getAnnotation(EmokeRpcClient.class);
        EmokeMapping emokeGetMapping = method.getAnnotation(EmokeMapping.class);
       Class<?> fallback= emokeRpcClient.fallback();
        String zone = emokeRpcClient.zone();
        String api = emokeGetMapping.requestMapping();
        Class<?> returnType = method.getReturnType();
        Parameter[] parameters = method.getParameters();

//        System.out.println("parameters=====" + parameters.length);
//        System.out.println("主域名:" + zone);
//        System.out.println("api:" + api);
//        System.out.println("requestMethod:" + requestMethod);
//        System.out.println("主类是:" + declaringClass);
//        System.out.println("方法是:" + method.getName());
//        System.out.println("返回对象是:" + returnType.getName());
        Map<String, Object> getMap = new HashMap<>();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            getMap.put(parameter.getName(), args[i].toString());
        }
        long s1=System.currentTimeMillis();
        try {
            switch (emokeGetMapping.method()) {
                case GET:
                    return HttpUtil.getInstance().get(zone + api, getMap, Class.forName(returnType.getName()));
                case PUT:

                case POST:
                    return HttpUtil.getInstance().post(zone + api, getMap, Class.forName(returnType.getName()));
                case DELETE:
                    break;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            System.out.println("ssss===="+(System.currentTimeMillis()-s1));
            Object fallbackObj=null;
            try {
                 fallbackObj=  fallback.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
            if(e instanceof ConnectException){
              //获取fallback
                try {
                  return   fallback.getMethod("onException", RpcException.class).invoke(fallbackObj,new RpcException(RpcException.Type.ConnectionException));
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                    ex.printStackTrace();
                }
            }else if(e instanceof SocketTimeoutException){
                //获取fallback
                try {
                    return   fallback.getMethod("onException", RpcException.class).invoke(fallbackObj,new RpcException(RpcException.Type.ConnectTimeoutException));
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                    ex.printStackTrace();
                }
           }
        }

        return null;
    }


}
