package com.emoke.core.rpc.http;

import com.alibaba.fastjson.JSON;
import okhttp3.*;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpUtil {
    private OkHttpClient okHttpClient;

    private OkHttpClient getOkHttpClient() {
        if (null == okHttpClient) {
            synchronized (HttpUtil.class) {
                if (null == okHttpClient) {
                    okHttpClient = new OkHttpClient.Builder()
                            //设置连接超时时间
                            .connectTimeout(15, TimeUnit.SECONDS)
                            //设置读取超时时间
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();
                }
            }


        }

        return okHttpClient;
    }

    private static HttpUtil httpUtil;

    public static HttpUtil getInstance() {
        if (null == httpUtil) {
            synchronized (HttpUtil.class) {
                if (null == httpUtil)
                    httpUtil = new HttpUtil();

            }
        }
        return httpUtil;
    }

    /**
     * get请求
     *
     * @param url:请求url
     * @param cls:泛型
     */
    public Object get(String url, Map<String, Object> paramMap, Class<?> cls) throws IOException {

        Request.Builder requestBuilder = new Request.Builder();

        StringBuilder urlBuilder = new StringBuilder(url);
        if (paramMap != null) {
            urlBuilder.append("?");
            try {
                for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                    urlBuilder.append(URLEncoder.encode(entry.getKey(), "utf-8")).
                            append("=").
                            append(URLEncoder.encode(entry.getValue().toString(), "utf-8")).
                            append("&");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }

        requestBuilder.url(urlBuilder.toString()).get();
        final Request request = requestBuilder.build();
        Call call = getOkHttpClient().newCall(request);
            Response response = call.execute();
            if (response.isSuccessful()) {
                ResponseBody responseBody = response.body();
                if (null != responseBody) {
                    String resp = responseBody.string();
                    return JSON.parseObject(resp, cls);
                }
            } else {

                return null;
            }

        return null;
    }

    /**
     * 登陆考拉
     *
     * @param url:旷视盒子api地址
     * @param mapParams:post参数
     */
    public Object post(String url, Map<String, Object> mapParams, Class<?> cls) throws SocketTimeoutException {
        FormBody.Builder form = new FormBody.Builder();
        for (String key : mapParams.keySet()) {
            form.add(key, mapParams.get(key) + "");
        }
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(form.build());
        final Request request = requestBuilder.build();
        Call call = getOkHttpClient().newCall(request);

        try {
            Response response = call.execute();
            //3.对返回结果进行处理
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                return JSON.parseObject(body.string(), cls);
            }
        } catch (SocketTimeoutException e) {
            throw new SocketTimeoutException();
        } catch (IOException e1) {

        }
        return null;
    }



}
