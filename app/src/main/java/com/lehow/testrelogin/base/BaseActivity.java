package com.lehow.testrelogin.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import dagger.android.AndroidInjection;

/**
 * desc:
 * author: luoh17
 * time: 2018/10/10 16:49
 */
public class BaseActivity extends AppCompatActivity {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
  }
}
