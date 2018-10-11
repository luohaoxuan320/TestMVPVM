package com.lehow.testrelogin.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.lehow.testrelogin.base.BaseActivity;
import com.lehow.testrelogin.login.LoginActivity;
import com.lehow.testrelogin.R;
import com.lehow.testrelogin.net.NetApi;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.functions.Consumer;
import javax.inject.Inject;

import static com.uber.autodispose.AutoDispose.autoDisposable;

public class DetailActivity extends BaseActivity {

  TextView tvInfo;
  Button btnAdd;
  DetailViewModel detailViewModel;

  @Inject NetApi netApi;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i("TAG", "onCreate: netApi=" + netApi);

    //Log.i("TAG", "onCreate: "+ DaggerNetComponent.create().getNetApi());
    setContentView(R.layout.activity_detail);
    tvInfo = findViewById(R.id.tv_info);
    btnAdd = findViewById(R.id.btn_add);
    detailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);

    detailViewModel.getValueInfo()
        .as(AutoDispose.<Long>autoDisposable(AndroidLifecycleScopeProvider.from(getLifecycle())))
        .subscribe(new Consumer<Long>() {
          @Override public void accept(Long integer) throws Exception {
            tvInfo.setText(integer + "");
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
}
