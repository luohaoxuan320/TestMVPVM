package com.lehow.testrelogin.di;

import com.lehow.testrelogin.detail.DetailActivity;
import com.lehow.testrelogin.login.LoginActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * desc:
 * author: luoh17
 * time: 2018/10/10 16:45
 */
@Module(subcomponents = { BaseActivityComponent.class })
public abstract class ActivityBuildersModule {

  @ContributesAndroidInjector(modules = LoginModule.class)
  abstract LoginActivity bindLoginActivity();

  @ContributesAndroidInjector() abstract DetailActivity bindDetailActivity();
}
