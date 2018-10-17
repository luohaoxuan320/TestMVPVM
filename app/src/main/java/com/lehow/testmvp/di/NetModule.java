package com.lehow.testmvp.di;

import android.util.Log;
import com.lehow.net.ApiStateException;
import com.lehow.net.RxTransformer;
import com.lehow.net.converter.GsonConverterFactory;
import com.lehow.testmvp.BuildConfig;
import com.lehow.testmvp.net.NetApi;
import dagger.Module;
import dagger.Provides;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.functions.Function;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.reactivestreams.Publisher;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * desc:
 * author: luoh17
 * time: 2018/10/10 14:58
 */
@Module(includes = NetViewModelModule.class) public class NetModule {

  @Singleton @Provides NetApi provideNetApi(Retrofit retrofit) {
    Log.i("TAG", "provideNetApi: " + retrofit);
    final NetApi originNetApi = retrofit.create(NetApi.class);
    NetApi netApi = (NetApi) Proxy.newProxyInstance(originNetApi.getClass().getClassLoader(),
        originNetApi.getClass().getInterfaces(), new InvocationHandler() {

          @Override public Object invoke(Object proxy, final Method method, final Object[] args)
              throws Throwable {
            return Maybe.just(true).flatMap(new Function<Boolean, MaybeSource<?>>() {
              @Override public MaybeSource<?> apply(Boolean aBoolean) throws Exception {
                Maybe<?> invoke = (Maybe<?>) method.invoke(originNetApi, args);//执行原来的方法
                return invoke;
              }
            }).compose(RxTransformer.io_main()).compose(filterRelogin());
          }
        });
    return netApi;
  }

  @Singleton @Provides OkHttpClient provideOkHttpClient(HttpLoggingInterceptor loggingInterceptor) {
    return new OkHttpClient.Builder().addInterceptor(loggingInterceptor).build();
  }

  @Provides Retrofit provideRetrofit(OkHttpClient okHttpClient) {
    //Log.i("TAG", "provideRetrofit: "+okHttpClient);
    return new Retrofit.Builder().baseUrl(BuildConfig.HOST)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build();
  }

  @Provides HttpLoggingInterceptor provideLoggingInterceptor() {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    return loggingInterceptor;
  }

  private <T> MaybeTransformer filterRelogin() {
    return new MaybeTransformer<T, T>() {
      @Override public MaybeSource<T> apply(Maybe<T> upstream) {

        return upstream.retryWhen(new Function<Flowable<Throwable>, Publisher<?>>() {
          @Override public Publisher<?> apply(Flowable<Throwable> throwableFlowable)
              throws Exception {
            return throwableFlowable.flatMap(new Function<Throwable, Publisher<?>>() {
              @Override public Publisher<?> apply(Throwable throwable) throws Exception {
                if (throwable instanceof ApiStateException) {
                  int errCode = ((ApiStateException) throwable).getCode();
                  //toastRelogin(errCode);

                }
                return Maybe.error(throwable).toFlowable();//直接返回错误
              }
            });
          }
        });
      }
    };
  }
}
