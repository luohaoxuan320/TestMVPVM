package com.lehow.testrelogin.net;

import com.lehow.testrelogin.login.LoginResult;
import io.reactivex.Maybe;
import java.util.HashMap;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * desc:
 * author: luoh17
 * time: 2018/9/23 20:20
 */
public interface NetApi {

  @POST("managerApi/managerLogin") Maybe<LoginResult.SaleInfo> managerLogin(
      @Body HashMap<String, String> loginInfo);
}

