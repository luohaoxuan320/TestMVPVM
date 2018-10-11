package com.lehow.testrelogin.detail;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

/**
 * desc:
 * author: luoh17
 * time: 2018/10/9 13:40
 */
public class DetailViewModel extends ViewModel {

  //BehaviorSubject<Integer> valueInfo =BehaviorSubject.createDefault(0);

  Observable<Long> valueInfo =
      Observable.interval(0, 1, TimeUnit.SECONDS).take(60).map(new Function<Long, Long>() {
        @Override public Long apply(Long aLong) throws Exception {
          Log.i("TAG", "apply: " + aLong);
          return 60 - aLong;
        }
      });

  public Observable<Long> getValueInfo() {
    return valueInfo.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
  }

  public void add() {
    Log.i("TAG", "add: ");
    //valueInfo.onNext(valueInfo.getValue()+1);
  }
}
