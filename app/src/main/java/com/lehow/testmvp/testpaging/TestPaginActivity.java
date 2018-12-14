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
    pagingViewModel.pagedListObservable.subscribe(new Consumer<PagedList<PageEntity>>() {
      @Override public void accept(PagedList<PageEntity> pageEntities) throws Exception {
        for (int i = 0; i < pageEntities.size(); i++) {
          Log.i("TAG",
              "accept: entity id=" + pageEntities.get(i).getId() + " name=" + pageEntities.get(i)
                  .getName());
        }
        myPagingAdapter.submitList(pageEntities);
      }
    }, new Consumer<Throwable>() {
      @Override public void accept(Throwable throwable) throws Exception {
        Log.i("TAG", "accept: " + throwable);
      }
    });
  }
}
