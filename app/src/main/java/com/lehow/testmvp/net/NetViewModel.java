package com.lehow.testmvp.net;

import android.arch.lifecycle.ViewModel;

/**
 * desc:
 * author: luoh17
 * time: 2018/10/11 19:00
 */
public abstract class NetViewModel extends ViewModel {
  protected NetApi netApi;

  public NetViewModel(NetApi netApi) {
    this.netApi = netApi;
  }
}
