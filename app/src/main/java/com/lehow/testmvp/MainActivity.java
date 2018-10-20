package com.lehow.testmvp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.lehow.testmvp.detail.DetailActivity;
import com.lehow.testmvp.login.LoginActivity;
import com.lehow.testmvp.testfrg.FrgActivity;

public class MainActivity extends AppCompatActivity {


  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

  }

  public void btnClick(View view) {
    startActivity(new Intent(this,DetailActivity.class));

  }

  public void btnRelogin(View view) {
    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    getApplicationContext().startActivity(intent);
  }

  public void btnFragment(View view) {
    Intent intent = new Intent(this, FrgActivity.class);
    startActivity(intent);
  }
}
