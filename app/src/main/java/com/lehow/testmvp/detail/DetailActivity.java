package com.lehow.testmvp.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.lehow.net.ILoadingView;
import com.lehow.net.NetMaybeObservable;
import com.lehow.net.RxTransformer;
import com.lehow.testmvp.base.BaseActivity;
import com.lehow.testmvp.login.LoginActivity;
import com.lehow.testmvp.R;
import com.lehow.testmvp.net.NetApi;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

import static com.lehow.net.RxTransformer.io_main;
import static com.uber.autodispose.AutoDispose.autoDisposable;

public class DetailActivity extends BaseActivity implements ILoadingView {

  TextView tvInfo;
  Button btnAdd;
  DetailViewModel detailViewModel;


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //Log.i("TAG", "onCreate: "+ DaggerNetComponent.create().getNetApi());
    setContentView(R.layout.activity_detail);
    tvInfo = findViewById(R.id.tv_info);
    btnAdd = findViewById(R.id.btn_add);
    detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);

    detailViewModel.getValueInfo().compose(RxTransformer.<Long>waitLoading(this))
        .as(AutoDispose.<Long>autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
        .subscribe(new Consumer<Long>() {
          @Override public void accept(Long aLong) throws Exception {
            Log.i("TAG", "accept: aLong=" + aLong);
            tvInfo.setText(aLong + "");
          }
        });
  }


  public void btnRelogin(View view) {
    Intent intent = new Intent(this, LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    //由于LoginActivity被 finish了，所以这个没啥用
    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
  }

  public void clickAdd(View view) {
  }

  @Override public void dismiss() {

  }

  @Override public void show() {

  }
}
