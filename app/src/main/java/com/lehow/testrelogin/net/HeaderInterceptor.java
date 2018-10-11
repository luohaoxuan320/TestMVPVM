package com.lehow.testrelogin.net;

import com.lehow.net.BuildConfig;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * desc:请求的拦截器，添加通用Header
 * author: luoh17
 * time: 2018/9/29 11:32
 */
public class HeaderInterceptor implements Interceptor {
  @Override public Response intercept(Chain chain) throws IOException {
    Request original = chain.request();
    // Request customization: add request headers
    Request.Builder requestBuilder = original.newBuilder()
        .addHeader("pushId", "")
        .addHeader("clientType", "2")
        .addHeader("version", BuildConfig.VERSION_NAME)
        .addHeader("token", "")
        .addHeader("verName", "sales")
        .addHeader("appToken", "");

    Request request = requestBuilder.build();
    return chain.proceed(request);
  }
}
