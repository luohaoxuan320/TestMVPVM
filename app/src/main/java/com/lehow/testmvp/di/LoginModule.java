package com.lehow.testmvp.di;

import dagger.Module;
import dagger.Provides;

/**
 * desc:
 * author: luoh17
 * time: 2018/10/10 14:53
 */
@Module public class LoginModule {

  @Provides String provideName() {
    return "TestName";
  }
}
