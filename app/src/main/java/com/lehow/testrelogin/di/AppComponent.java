package com.lehow.testrelogin.di;

import android.app.Application;
import com.lehow.testrelogin.MyApplication;
import com.lehow.testrelogin.net.NetApi;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;

/**
 * desc:
 * author: luoh17
 * time: 2018/10/10 17:46
 */
@Singleton @Component(modules = {
    AndroidInjectionModule.class, AndroidSupportInjectionModule.class, ActivityBuildersModule.class,
    NetModule.class
}) public interface AppComponent {
  /*  @Component.Builder
    interface Builder {
      @BindsInstance
      Builder application(Application application);

      AppComponent build();
    }*/
  void inject(MyApplication application);
}



