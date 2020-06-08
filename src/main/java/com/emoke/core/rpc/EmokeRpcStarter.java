package com.emoke.core.rpc;

import com.emoke.core.rpc.annotation.EmokeRpcService;
import com.emoke.core.rpc.proxy.EmokeProxy;
import com.emoke.core.rpc.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.List;

public class EmokeRpcStarter {
    private String scanPackage;

    public EmokeRpcStarter(String scanPackage) {
        this.scanPackage = scanPackage;
//        try {
//            initRpc();
//        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 初始化rpc，生成rpc的动态代理
     */
    private void initRpc() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        List<String> rpcPackages = ReflectUtil.getClazzName(scanPackage, true);
        if (rpcPackages.size() == 0)
            return;
        for (String rpcPackage : rpcPackages) {
            Class<?> cls = Class.forName(rpcPackage);
            Field[] fields = cls.getDeclaredFields();
            if (fields.length == 0)
                continue;
            for (Field field : fields) {
                if (!field.isAnnotationPresent(EmokeRpcService.class))
                    continue;
                System.out.println("0000000000====="+field.getName());
                Class<?> type= field.getType();
                System.out.println("-----type----------" + type);
                String interfacesStr=field.getGenericType().toString().substring(10);
                System.out.println("interfacesStr====="+interfacesStr);
                //反射获取接口类型
                Class<?> ifc=Class.forName(interfacesStr);
                field.setAccessible(true);
                Object obj=EmokeProxy.createProxy(ifc);
          //      System.out.println("$$$$$########"+ConvertUtils.convert(obj,type).getClass());
           //     Object o=ConvertUtils.convert(obj,type);
//                try {
//                    o.getClass().getMethod("say",new Class[]{}).invoke(cls,new Object[]{});
//                } catch (InvocationTargetException e) {
//                    e.printStackTrace();
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                }

             //   field.set(cls, ConvertUtils.convert(obj,type));
            }
        }
    }

}
