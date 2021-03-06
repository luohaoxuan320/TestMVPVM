package com.lehow.testmvp.login;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.lehow.net.NetMaybeObservable;
import com.lehow.net.RxTransformer;
import com.lehow.testmvp.MainActivity;
import com.lehow.testmvp.R;
import com.lehow.testmvp.base.BaseActivity;
import com.lehow.testmvp.testpaging.TestPaginActivity;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
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
        .as(AutoDispose.<LoginResult.SaleInfo>autoDisposable(
            AndroidLifecycleScopeProvider.from(getLifecycle())))
        .subscribe(new NetMaybeObservable<LoginResult.SaleInfo>() {
          @Override public void onSuccess(LoginResult.SaleInfo saleInfo) {
            Log.i("TAG", "onSuccess: " + saleInfo);
          }

          @Override public void onError(Throwable e) {
            Log.i("TAG", "onError: " + e);
          }
        });
    startActivity(new Intent(this, MainActivity.class));
    finish();
  }

  public void btnTestPaging(View view) {
    startActivity(new Intent(this, TestPaginActivity.class));
  }
}
