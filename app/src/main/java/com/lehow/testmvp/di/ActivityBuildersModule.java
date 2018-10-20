package com.lehow.testmvp.di;

import com.lehow.testmvp.detail.DetailActivity;
import com.lehow.testmvp.login.LoginActivity;
import com.lehow.testmvp.testfrg.FrgActivity;
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

  @ContributesAndroidInjector() abstract FrgActivity bindFrgActivity();
}
