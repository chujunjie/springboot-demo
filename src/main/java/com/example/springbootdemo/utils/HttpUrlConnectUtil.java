package com.example.springbootdemo.utils;


import com.example.springbootdemo.common.Const;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.net.ProtocolException;
import java.util.concurrent.TimeUnit;

/**
 * httpUrl有效性检测工具
 */
@Slf4j
public class HttpUrlConnectUtil {


    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .connectionPool(new ConnectionPool(25, 5, TimeUnit.MINUTES))
            .build();

    /**
     * 进行url拼接访问
     * @param prefixUrl oss服务器的前缀
     * @param suffixUrl 存储在表中的后缀
     */
    public static int testUrlBoolIsConnect (String prefixUrl,String suffixUrl) {

        String url = prefixUrl + suffixUrl;

        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            int statusCode = response.code();
            if (statusCode == Const.STATUS_NORMAL) {
                response.close();
                return Const.RESULT_OK;
            }
            response.close();
            return Const.RESULT_FAILURE;

        } catch (ProtocolException e) {

            return Const.RESULT_FAILURE;

        } catch (Exception e) {

            log.error(e.getLocalizedMessage(), e);
            return Const.RESULT_FAILURE;
        }
    }

}
