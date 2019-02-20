package com.lehow.testmvp.testpaging;

import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import butterknife.BindView;
import com.lehow.testmvp.R;
import com.lehow.testmvp.base.BaseActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class TestPaginActivity extends BaseActivity {

  @BindView(R.id.recyclerView) RecyclerView recyclerView;

  PagingViewModel pagingViewModel;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test_pagin);

    pagingViewModel = ViewModelProviders.of(this).get(PagingViewModel.class);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    final MyPagingAdapter myPagingAdapter = new MyPagingAdapter(new MyDifferUtil());
    recyclerView.setAdapter(myPagingAdapter);
    myPagingAdapter.bindViewModel(pagingViewModel);

  }
}
