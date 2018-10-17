package com.lehow.testmvp.net;

import java.io.IOException;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * desc: baseUrl拦截器，主要处理多host情况时切换
 * author: luoh17
 * time: 2018/9/29 9:06
 */
public class BaseUrlInterceptor implements Interceptor {
  final String header_switch_url = "switch_url";

  @Override public Response intercept(Chain chain) throws IOException {
    //获取request
    Request request = chain.request();
    //获取request的创建者builder
    Request.Builder builder = request.newBuilder();
    //从request中获取headers，通过给定的键url_name
    List<String> headerValues = request.headers(header_switch_url);
    if (headerValues != null && headerValues.size() > 0) {
      //如果有这个header，先将配置的header删除，因此header仅用作app和okhttp之间使用
      builder.removeHeader(header_switch_url);

      //匹配获得新的BaseUrl
      String headerValue = headerValues.get(0);

      //从request中获取原有的HttpUrl实例oldHttpUrl
      HttpUrl newFullUrl = request.url();
      //重建这个request，通过builder.url(newFullUrl).build()；
      //然后返回一个response至此结束修改
      return chain.proceed(builder.url(newFullUrl).build());
    } else {
      return chain.proceed(request);
    }
  }
}
