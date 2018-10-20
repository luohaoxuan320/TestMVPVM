package com.lehow.testmvp.testfrg;

import android.os.Bundle;
import com.lehow.testmvp.R;
import com.lehow.testmvp.base.BaseActivity;

public class FrgActivity extends BaseActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_frg);
    getSupportFragmentManager().beginTransaction()
        .add(R.id.base_content, new BlankFragment(), "tag")
        .commit();
  }
}
