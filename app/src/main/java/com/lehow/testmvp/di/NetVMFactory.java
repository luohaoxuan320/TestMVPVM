package com.lehow.testmvp.di;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import com.lehow.testmvp.net.NetApi;
import com.lehow.testmvp.net.NetViewModel;
import java.lang.reflect.InvocationTargetException;
import javax.inject.Inject;

/**
 * desc:
 * author: luoh17
 * time: 2018/10/11 18:55
 */
public class NetVMFactory implements ViewModelProvider.Factory {
  private NetApi netApi;

  @Inject public NetVMFactory(NetApi netApi) {
    this.netApi = netApi;
  }

  @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    if (NetViewModel.class.isAssignableFrom(modelClass)) {
      //noinspection TryWithIdenticalCatches
      try {
        return modelClass.getConstructor(NetApi.class).newInstance(netApi);
      } catch (NoSuchMethodException e) {
        throw new RuntimeException("Cannot create an instance of " + modelClass, e);
      } catch (IllegalAccessException e) {
        throw new RuntimeException("Cannot create an instance of " + modelClass, e);
      } catch (InstantiationException e) {
        throw new RuntimeException("Cannot create an instance of " + modelClass, e);
      } catch (InvocationTargetException e) {
        throw new RuntimeException("Cannot create an instance of " + modelClass, e);
      }
    }
    try {
      return modelClass.newInstance();
    } catch (InstantiationException e) {
      throw new RuntimeException("Cannot create an instance of " + modelClass, e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException("Cannot create an instance of " + modelClass, e);
    }
  }
}
