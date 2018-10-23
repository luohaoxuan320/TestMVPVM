package com.lehow.net;

import android.util.Log;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.CompletableTransformer;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.MaybeTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;

public class RxTransformer {

  private static final String TAG = "TAG";

  public static <T> IoMainTransformer<T> io_main() {
    return new IoMainTransformer();
  }

  public static <T> WaitingTransformer<T> waitLoading(final ILoadingView iLoadingView) {

    return new WaitingTransformer(iLoadingView);
  }

  public static <T> MaybeTransformer retryToken(final Maybe tokenObservable) {
    return new MaybeTransformer<T, T>() {
      @Override public MaybeSource<T> apply(Maybe<T> upstream) {

        return upstream.retryWhen(new Function<Flowable<Throwable>, Publisher<?>>() {
          @Override public Publisher<?> apply(Flowable<Throwable> throwableFlowable)
              throws Exception {
            return throwableFlowable.flatMap(new Function<Throwable, Publisher<?>>() {
              @Override public Publisher<?> apply(Throwable throwable) throws Exception {
                if (throwable instanceof ApiStateException) {
                  int errCode = ((ApiStateException) throwable).getCode();
                  if (errCode == ApiStateException.ERR_ACCESS_TOKEN) {
                    Log.i(TAG, "retryToken 等待Token刷新: " + Thread.currentThread());
                    return TokenRefresh.getInstance()
                        .refresh(tokenObservable.toObservable())
                        .toFlowable(BackpressureStrategy.DROP);//等待刷新token
                  } else if (errCode == ApiStateException.ERR_REFRESH_TOKEN) {
                    //退回登录
                  }
                }
                return Maybe.error(throwable).toFlowable();//直接返回错误
              }
            });
          }
        });
      }
    };
  }

  public static <T> ObservableTransformer retryToken(final Observable tokenObservable) {
    return new ObservableTransformer<T, T>() {

      @Override public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
          @Override public ObservableSource<?> apply(Observable<Throwable> throwableObservable)
              throws Exception {
            Log.i(TAG, "retryToken  retryWhen: " + Thread.currentThread());
            return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
              @Override public ObservableSource<?> apply(final Throwable throwable)
                  throws Exception {
                if (throwable instanceof ApiStateException) {
                  int errCode = ((ApiStateException) throwable).getCode();
                  if (errCode == ApiStateException.ERR_ACCESS_TOKEN) {
                    Log.i(TAG, "retryToken 等待Token刷新: " + Thread.currentThread());
                    return TokenRefresh.getInstance().refresh(tokenObservable);//等待刷新token
                  } else if (errCode == ApiStateException.ERR_REFRESH_TOKEN) {
                    //退回登录

                  }
                }
                return Observable.error(throwable);//直接返回错误
              }
            });
          }
        });
      }
    };
  }

  static class IoMainTransformer<T>
      implements MaybeTransformer<T, T>, ObservableTransformer<T, T>, CompletableTransformer,
      FlowableTransformer<T, T> {

    @Override public CompletableSource apply(Completable upstream) {
      Log.i(TAG, "IoMain:thread=" + Thread.currentThread());
      return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override public Publisher<T> apply(Flowable<T> upstream) {
      Log.i(TAG, "IoMain:thread=" + Thread.currentThread());
      return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override public MaybeSource<T> apply(Maybe<T> upstream) {
      Log.i(TAG, "IoMain:thread=" + Thread.currentThread());
      return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override public ObservableSource<T> apply(Observable<T> upstream) {
      Log.i(TAG, "IoMain:thread=" + Thread.currentThread());
      return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
  }

  static class WaitingTransformer<T>
      implements MaybeTransformer<T, T>, ObservableTransformer<T, T>, CompletableTransformer,
      FlowableTransformer<T, T> {

    ILoadingView loadingView;

    public WaitingTransformer(ILoadingView iLoadingView) {
      this.loadingView = iLoadingView;
    }

    @Override public CompletableSource apply(Completable upstream) {
      UsingHandle usingHandle = new UsingHandle<>(upstream, loadingView);
      return Completable.using(usingHandle, usingHandle, usingHandle);
    }

    @Override public Publisher apply(Flowable upstream) {
      UsingHandle usingHandle = new UsingHandle<>(upstream, loadingView);
      return Flowable.using(usingHandle, usingHandle, usingHandle);
    }

    @Override public MaybeSource apply(final Maybe upstream) {
      UsingHandle usingHandle = new UsingHandle<>(upstream, loadingView);
      return Maybe.using(usingHandle, usingHandle, usingHandle);
    }

    @Override public ObservableSource apply(Observable upstream) {
      UsingHandle usingHandle = new UsingHandle<>(upstream, loadingView);
      return Observable.using(usingHandle, usingHandle, usingHandle);
    }
  }

  static class UsingHandle<T>
      implements Callable<ILoadingView>, Function<ILoadingView, T>, Consumer<ILoadingView> {

    ILoadingView iLoadingView;

    T upstream;

    public UsingHandle(T upstream, ILoadingView loadingView) {
      this.upstream = upstream;
      iLoadingView = loadingView;
    }

    @Override public void accept(ILoadingView loadingView) throws Exception {
      Log.i(TAG, "WaitingTransformer dismiss: " + Thread.currentThread());
      iLoadingView.dismiss();
    }

    @Override public T apply(ILoadingView loadingView) throws Exception {
      Log.i(TAG, "WaitingTransformer dowork: " + Thread.currentThread());
      return upstream;
    }

    @Override public ILoadingView call() throws Exception {
      Log.i(TAG, "WaitingTransformer show: " + Thread.currentThread());
      iLoadingView.show();
      return iLoadingView;
    }
  }
}
