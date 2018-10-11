package com.lehow.testrelogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.lehow.testrelogin.login.LoginActivity;

public class FlashActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_flash);
  }

  public void btnClick(View view) {
    startActivity(new Intent(this,LoginActivity.class));
    finish();
  }
}
