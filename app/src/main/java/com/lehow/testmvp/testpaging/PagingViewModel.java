package com.lehow.testmvp.testpaging;

import android.arch.lifecycle.ViewModel;
import android.arch.paging.DataSource;
import android.arch.paging.ItemKeyedDataSource;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;
import android.arch.paging.RxPagedListBuilder;
import android.support.annotation.NonNull;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;

/**
 * desc:
 * author: luoh17
 * time: 2018/10/20 18:59
 */
public class PagingViewModel extends ViewModel {

  DataSource.Factory<Integer, PageEntity> pagerListFactory;
  Observable<PagedList<PageEntity>> pagedListObservable;

  public static final int state_loading = 1;
  public static final int state_retry = 2;
  public static final int state_finish = 3;

  private int retryCount = 0;
  PublishSubject<Integer> loadMoreState = PublishSubject.create();

  Observable retrySubject = null;
  public PagingViewModel() {
    this.pagerListFactory = new DataSource.Factory<Integer, PageEntity>() {
      @Override public DataSource<Integer, PageEntity> create() {
        return new PagingDataSource();
      }
    };

    pagedListObservable = new RxPagedListBuilder(pagerListFactory, 20).buildObservable();
  }

  class ItemDataSource extends ItemKeyedDataSource<Integer, PageEntity> {
    @Override public void loadInitial(@NonNull LoadInitialParams<Integer> params,
        @NonNull LoadInitialCallback<PageEntity> callback) {
      Log.i("TAG", "loadInitial: key="
          + params.requestedInitialKey
          + "  requestedLoadSize="
          + params.requestedLoadSize
          + " placeholdersEnabled="
          + params.placeholdersEnabled);
      ArrayList<PageEntity> pageEntities = new ArrayList<>();
      int start = 0;
      for (Integer i = 0; i < params.requestedLoadSize; i++) {
        pageEntities.add(new PageEntity(start + i, "T_" + (start + i)));
      }
      callback.onResult(pageEntities);
    }

    @Override public void loadAfter(@NonNull LoadParams<Integer> params,
        @NonNull LoadCallback<PageEntity> callback) {
      Log.i("TAG",
          "loadAfter: key=" + params.key + "  requestedLoadSize=" + params.requestedLoadSize);
     /* ArrayList<PageEntity> pageEntities = new ArrayList<>();
      int start = params.key * params.requestedLoadSize;
      for (Integer i = 0; i < params.requestedLoadSize; i++) {
        pageEntities.add(new PageEntity((start+i), "T_" + (start+i)));
      }
      callback.onResult(pageEntities);*/
      //callback.onResult(null);
    }

    @Override public void loadBefore(@NonNull LoadParams<Integer> params,
        @NonNull LoadCallback<PageEntity> callback) {
      Log.i("TAG",
          "loadBefore:key=" + params.key + "  requestedLoadSize=" + params.requestedLoadSize);
     /* int start =0;
      ArrayList<PageEntity> pageEntities = new ArrayList<>();
      for (Integer i = 0; i < params.requestedLoadSize; i++) {
        pageEntities.add(new PageEntity(start+i, "T_" + (start+i)));
      }
      callback.onResult(pageEntities);*/
    }

    @NonNull @Override public Integer getKey(@NonNull PageEntity item) {
      return item.getId();
    }
  }

  class PagingDataSource extends PageKeyedDataSource<Integer, PageEntity> {

    @Override public void loadInitial(@NonNull LoadInitialParams<Integer> params,
        @NonNull LoadInitialCallback<Integer, PageEntity> callback) {
      Log.i("TAG", "loadInitial: requestedLoadSize="
          + params.requestedLoadSize
          + " "
          + params.placeholdersEnabled);
      ArrayList<PageEntity> pageEntities = new ArrayList<>();
      for (Integer i = 0; i < 20; i++) {
        pageEntities.add(new PageEntity(i, "T_" + (i)));
      }
      callback.onResult(pageEntities, null, 1);
    }

    @Override public void loadBefore(@NonNull LoadParams<Integer> params,
        @NonNull LoadCallback<Integer, PageEntity> callback) {
      Log.i("TAG",
          "loadBefore: key=" + params.key + " requestedLoadSize=" + params.requestedLoadSize);
    }

    @Override public void loadAfter(@NonNull final LoadParams<Integer> params,
        @NonNull final LoadCallback<Integer, PageEntity> callback) {

      loadMoreState.onNext(state_loading);
      retrySubject = null;
      int start = 0;

      Log.i("TAG", "loadAfter: key="
          + params.key
          + " requestedLoadSize="
          + params.requestedLoadSize
          + "  start=" + start + "  Thread=" + Thread.currentThread());

      int offset = params.key * params.requestedLoadSize;

      try {
        Thread.sleep(3 * 1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      if (retryCount < 3 && params.key == 2) {
        loadMoreState.onNext(state_retry);
        retrySubject = Observable.create(new ObservableOnSubscribe() {
          @Override public void subscribe(ObservableEmitter emitter) throws Exception {
            Log.i("TAG", "subscribe: 重新获取请求");
            retryCount++;
            loadAfter(params, callback);
          }
        }).subscribeOn(Schedulers.io());//不然loadAfter里的sleep会阻塞主线程
        return;
      }
      ArrayList<PageEntity> pageEntities = new ArrayList<>();
      for (Integer i = start; i < params.requestedLoadSize; i++) {
        pageEntities.add(new PageEntity(start, "T_" + (offset + i)));
      }
      callback.onResult(pageEntities, params.key + 1);
      loadMoreState.onNext(state_finish);
    }
  }
}

