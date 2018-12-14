package com.lehow.testmvp.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lehow.net.PagerJsonSerializer;
import com.lehow.net.PagerReqMix;
import com.lehow.net.converter.GsonConverterFactory;
import com.lehow.net.interceptor.NetMockedInterceptor;
import com.lehow.testmvp.BuildConfig;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/14 16:56
 */
public class NetHelper {
  private Retrofit retrofit;
  public static NetHelper instance = new NetHelper();

  private NetHelper() {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    retrofit = new Retrofit.Builder().baseUrl(BuildConfig.HOST)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(getGson()))
        .client(new OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            //.addInterceptor(new NetMockedInterceptor(BuildConfig.HOST,BuildConfig.URL_MOCK))
            //.addInterceptor(new NetMockedInterceptor(BuildConfig.HOST,BuildConfig.URL_MOCK,new String[]{"你要mock拦截的接口列表"}))
            .addInterceptor(new BaseUrlInterceptor())
            .addInterceptor(new HeaderInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build())
        .build();
  }

  public <T> T create(final Class<T> service) {
    return retrofit.create(service);
  }

  public Gson getGson() {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.registerTypeAdapter(PagerReqMix.class, PagerJsonSerializer.class);
    return gsonBuilder.create();
  }
}
