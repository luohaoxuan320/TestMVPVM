package com.lehow.testrelogin.login;

import com.lehow.testrelogin.net.NetApi;
import com.lehow.testrelogin.net.NetViewModel;
import io.reactivex.Maybe;
import java.util.HashMap;

/**
 * desc:
 * author: luoh17
 * time: 2018/10/9 20:34
 */
public class LoginViewModel extends NetViewModel {

  public LoginViewModel(NetApi netApi) {
    super(netApi);
  }

  public Maybe<LoginResult.SaleInfo> login(String phone, String passWord) {
    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("loginPhone", phone);
    hashMap.put("pwd", passWord);
    return netApi.managerLogin(hashMap);
  }
}
