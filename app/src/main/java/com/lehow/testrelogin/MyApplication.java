package com.lehow.testrelogin;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;
import com.lehow.testrelogin.di.AppInject;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import javax.inject.Inject;

/**
 * desc:
 * author: luoh17
 * time: 2018/10/10 11:02
 */
public class MyApplication extends Application implements HasActivityInjector {
  @Inject DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

  @Override public void onCreate() {
    super.onCreate();
    AppInject.init(this);
  }

  @Override public AndroidInjector<Activity> activityInjector() {
    return dispatchingAndroidInjector;
  }
}
