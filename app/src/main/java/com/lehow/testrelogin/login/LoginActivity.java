package com.lehow.testrelogin.login;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.lehow.net.NetMaybeObservable;
import com.lehow.testrelogin.R;
import com.lehow.testrelogin.base.BaseActivity;
import javax.inject.Inject;

public class LoginActivity extends BaseActivity {

  @Inject ViewModelProvider.Factory netFactory;
  @Inject String name;
  LoginViewModel loginViewModel;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    loginViewModel = ViewModelProviders.of(this, netFactory).get(LoginViewModel.class);
    //netApi= DaggerNetComponent.create().getNetApi();
    Log.i("TAG", "onCreate: name=" + name);
    //Log.i("TAG", "onCreate: getNetApi="+ DaggerNetComponent.create().getNetApi());
  }

  public void btnLogin(View view) {
    loginViewModel.login("18316856957", "xsj123")
        .subscribe(new NetMaybeObservable<LoginResult.SaleInfo>() {
          @Override public void onSuccess(LoginResult.SaleInfo saleInfo) {
            Log.i("TAG", "onSuccess: " + saleInfo);
          }

          @Override public void onError(Throwable e) {
            Log.i("TAG", "onError: " + e);
          }
        });
    //startActivity(new Intent(this,MainActivity.class));
    //finish();
  }
}
