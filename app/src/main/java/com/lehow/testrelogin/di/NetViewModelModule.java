package com.lehow.testrelogin.di;

import android.arch.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import javax.inject.Singleton;

/**
 * desc:注入NetVMFactory，实例化NetViewModel
 * author: luoh17
 * time: 2018/10/11 19:25
 */

@Module public abstract class NetViewModelModule {
  @Singleton @Binds
  abstract ViewModelProvider.Factory bindNetViewModelFactory(NetVMFactory factory);
}
