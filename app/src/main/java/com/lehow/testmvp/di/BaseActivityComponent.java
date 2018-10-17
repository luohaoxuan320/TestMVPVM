package com.lehow.testmvp.di;

import com.lehow.testmvp.base.BaseActivity;
import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

/**
 * desc:
 * author: luoh17
 * time: 2018/10/10 16:48
 */
@Subcomponent(modules = { AndroidInjectionModule.class }) public interface BaseActivityComponent
    extends AndroidInjector<BaseActivity> {

  @Subcomponent.Builder abstract class Builder extends AndroidInjector.Builder<BaseActivity> {

  }
}