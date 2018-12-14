package com.lehow.net.interceptor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * desc: baseUrl拦截器，主要处理多host情况时切换
 * author: luoh17
 * time: 2018/9/29 9:06
 */
public class NetMockedInterceptor implements Interceptor {

  private String url_host;
  private String url_mock;
  private String[] mockedPaths;

  public NetMockedInterceptor(String url_host, String url_mock, String[] mockedPaths) {
    this.url_host = url_host;
    this.url_mock = url_mock;
    this.mockedPaths = mockedPaths;
  }

  public NetMockedInterceptor(String url_host, String url_mock) {
    this.url_host = url_host;
    this.url_mock = url_mock;
    try {
      Class<?> aClass = Class.forName("com.lehow.net.mock.MockedNetPaths");
      Method getMockNetPaths = aClass.getMethod("getMockNetPaths", null);
      mockedPaths = (String[]) getMockNetPaths.invoke(null);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  @Override public Response intercept(Chain chain) throws IOException {
    //获取request
    Request request = chain.request();

    if (mockedPaths != null && mockedPaths.length > 0) {
      HttpUrl oldUrl = request.url();
      for (String mockedPath : mockedPaths) {
        if (oldUrl.url().getPath().contains(mockedPath)) {
          //将url_host替换到url_mock
          String replace = oldUrl.url().toString().replace(url_host, url_mock);
          return chain.proceed(
              request.newBuilder().url(oldUrl.newBuilder(replace).build()).build());
        }
      }
    }
    return chain.proceed(request);
  }
}
