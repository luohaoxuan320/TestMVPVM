package com.lehow.testmvp;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.annotations.SerializedName;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.maybe.MaybeToPublisher;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import org.junit.Test;
import org.reactivestreams.Publisher;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

  int i = 0;

  @Test public void testRetry() {

    Maybe<Integer> just = Maybe.just(1);
    Maybe.error(new IllegalStateException())
        .onErrorResumeNext(new Function<Throwable, MaybeSource<?>>() {
          @Override public MaybeSource<?> apply(Throwable throwable) throws Exception {
            System.out.println("==onErrorResumeNext==");
            return Maybe.empty();
          }
        })
        .doOnError(new Consumer<Throwable>() {
          @Override public void accept(Throwable throwable) throws Exception {
            System.out.println("==doOnError==");
          }
        })
        .subscribe();

    final Observable<Boolean> toObservable =
        Maybe.just(1).flatMap(new Function<Integer, MaybeSource<Boolean>>() {
          @Override public MaybeSource<Boolean> apply(Integer integer) throws Exception {
            System.out.println("flatMap");
            return Maybe.error(new IllegalStateException());
          }
        }).toObservable();

    final PublishSubject<Boolean> publishSubject = PublishSubject.create();
    final Observable<Object> defer = Observable.defer(new Callable<ObservableSource<?>>() {
      @Override public ObservableSource<?> call() throws Exception {
        System.out.println("true");
        return Observable.just(true);
      }
    });
    Disposable subscribe = Observable.create(new ObservableOnSubscribe<Integer>() {
      @Override public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
        i++;
        System.out.println("create=" + i);
        if (i == 1) {
          emitter.onError(new IllegalAccessException());
        }
        emitter.onNext(i);
      }
    }).subscribeOn(Schedulers.io()).map(new Function<Integer, String>() {

      @Override public String apply(Integer integer) throws Exception {
        System.out.println("=====" + integer);
        return integer + "";
      }
    }).compose(new ObservableTransformer<String, String>() {
      @Override public ObservableSource<String> apply(Observable<String> upstream) {
        return upstream.retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
          @Override public ObservableSource<?> apply(Observable<Throwable> throwableObservable)
              throws Exception {
            return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
              @Override public ObservableSource<?> apply(Throwable throwable) throws Exception {
                System.out.println("retryWhen=" + throwable);
                return Observable.just(12);
              }
            });
          }
        });
      }
    }).doOnComplete(new Action() {
      @Override public void run() throws Exception {
        System.out.println("doOnComplete");
      }
    }).doOnDispose(new Action() {
      @Override public void run() throws Exception {
        System.out.println("doOnDispose");
      }
    }).subscribe(new Consumer<String>() {
      @Override public void accept(String s) throws Exception {
        System.out.println("accept: S=" + s);
      }
    }, new Consumer<Throwable>() {
      @Override public void accept(Throwable throwable) throws Exception {
        System.out.println("accept: throwable=" + throwable);
      }
    }, new Action() {
      @Override public void run() throws Exception {
        System.out.println("run: ");
      }
    });

    publishSubject.onComplete();
    //toObservable.subscribe(publishSubject);
    try {
      Thread.sleep(5 * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println(subscribe.isDisposed());
  }

  @Test public void testRetryMaybe() {

    final Maybe<Boolean> toObservable =
        Maybe.just(1).flatMap(new Function<Integer, MaybeSource<Boolean>>() {
          @Override public MaybeSource<Boolean> apply(Integer integer) throws Exception {
            return Maybe.just(true);
          }
        });

    final PublishSubject<Boolean> publishSubject = PublishSubject.create();
    final Observable<Object> defer = Observable.defer(new Callable<ObservableSource<?>>() {
      @Override public ObservableSource<?> call() throws Exception {
        System.out.println("true");
        return Observable.just(true);
      }
    });

    Maybe.create(new MaybeOnSubscribe<Integer>() {
      @Override public void subscribe(MaybeEmitter<Integer> emitter) throws Exception {
        i++;
        System.out.println("create=" + i);
        if (i == 1) {
          emitter.onError(new IllegalAccessException());
        }
        emitter.onSuccess(i);
      }
    }).map(new Function<Integer, String>() {

      @Override public String apply(Integer integer) throws Exception {
        System.out.println("=====" + integer);
        return integer + "";
      }
    }).retryWhen(new Function<Flowable<Throwable>, Publisher<?>>() {
      @Override public Publisher<?> apply(Flowable<Throwable> throwableFlowable) throws Exception {
        return throwableFlowable.flatMap(new Function<Throwable, Publisher<?>>() {
          @Override public Publisher<?> apply(Throwable throwable) throws Exception {
            //toObservable.toObservable().doOnNext(new Consumer<Boolean>() {
            //  @Override public void accept(Boolean aBoolean) throws Exception {
            //    System.out.println("doOnNext==="+aBoolean);
            //  }
            //}).subscribe(publishSubject);//立即执行并返回了
            //下面的publishSubject只能收到OnComplete事件

            return publishSubject.toFlowable(BackpressureStrategy.DROP)
                .doOnNext(new Consumer<Boolean>() {
                  @Override public void accept(Boolean aBoolean) throws Exception {
                    System.out.println("retryWhen doOnNext=" + aBoolean);
                  }
                })
                .doOnComplete(new Action() {
                  @Override public void run() throws Exception {
                    System.out.println("retryWhen doOnComplete");
                  }
                });
          }
        });
      }
    }).doOnComplete(new Action() {
      @Override public void run() throws Exception {
        System.out.println("doOnComplete");
      }
    }).doOnDispose(new Action() {
      @Override public void run() throws Exception {
        System.out.println("doOnDispose");
      }
    }).subscribe(new Consumer<String>() {
      @Override public void accept(String s) throws Exception {
        System.out.println("accept: S=" + s);
      }
    }, new Consumer<Throwable>() {
      @Override public void accept(Throwable throwable) throws Exception {
        System.out.println("accept: throwable=" + throwable);
      }
    }, new Action() {
      @Override public void run() throws Exception {
        System.out.println("run: ");
      }
    });

    //上面先返回，这里再订阅 ，就能触发onNext事件
    toObservable.toObservable().subscribe(publishSubject);
  }

  @Test public void testRetry2() {

    Observable.create(new ObservableOnSubscribe<Integer>() {
      @Override public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
        i++;
        System.out.println("create=" + i);
        if (i == 1) {
          emitter.onError(new IllegalAccessException());
        }
        emitter.onNext(i);
      }
    }).map(new Function<Integer, String>() {

      @Override public String apply(Integer integer) throws Exception {
        System.out.println("=====" + integer);
        return integer + "";
      }
    }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
      @Override public ObservableSource<?> apply(Observable<Throwable> throwableObservable)
          throws Exception {
        System.out.println("retryWhen" + throwableObservable);
        return Observable.just(111);
      }
    }).subscribe(new Consumer<String>() {
      @Override public void accept(String s) throws Exception {
        System.out.println("accept: S=" + s);
      }
    }, new Consumer<Throwable>() {
      @Override public void accept(Throwable throwable) throws Exception {
        System.out.println("accept: throwable=" + throwable);
      }
    }, new Action() {
      @Override public void run() throws Exception {
        System.out.println("run: ");
      }
    });
  }

  @Test public void addition_isCorrect() {

    GsonBuilder gsonBuilder = new GsonBuilder();
    JsonSerializer<PagerReq> pagerReqJsonSerializer = new JsonSerializer<PagerReq>() {
      @Override
      public JsonElement serialize(PagerReq src, Type typeOfSrc, JsonSerializationContext context) {
        //这个会导致死循环
        //context.serialize(src);
        final Gson gson = new Gson();
        //用标准的Gson对象来解析SerializedName，映射字段名，方便自定义json字段名
        JsonElement serialize = gson.toJsonTree(src);
        JsonObject asJsonObject = serialize.getAsJsonObject();
        JsonElement entity = asJsonObject.get("entity");
        //移除子节点
        asJsonObject.remove("entity");
        JsonObject entityAsJsonObject = entity.getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entries1 = entityAsJsonObject.entrySet();
        //将其挂到父节点
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : entries1) {
          asJsonObject.add(stringJsonElementEntry.getKey(), stringJsonElementEntry.getValue());
        }
        return serialize;
      }
    };
    gsonBuilder.registerTypeAdapter(PagerReq.class, pagerReqJsonSerializer);

    Gson customGson = gsonBuilder.create();

    PagerReq<User> userPagerReq = new PagerReq<>(1, 10, new User("AA", 18));
    HashMap<String, String> hashMap = new HashMap<>();
    hashMap.put("phone", "110");
    hashMap.put("name", "紧急电话");
    PagerReq<HashMap<String, String>> userPagerReqHashMap = new PagerReq<>(1, 10, hashMap);

    String json = customGson.toJson(userPagerReq);

    System.out.println(json);

    System.out.println(customGson.toJson(userPagerReqHashMap));



    assertEquals(4, 2 + 2);
  }

  @Test public void testSerializedName() {
    PagerReq<User> userPagerReq = new PagerReq<>(12, 20, new User("NNN", 18));
    System.out.println(new Gson().toJson(userPagerReq));
    ;
  }
  class PagerReq<T> {
    @SerializedName("PNO") private int pageNo;
    int pageSize;
    T entity;

    public PagerReq(int pageNo, int pageSize, T entity) {
      this.pageNo = pageNo;
      this.pageSize = pageSize;
      this.entity = entity;
    }
  }

  class User {
    String name;
    int age;

    public User(String name, int age) {
      this.name = name;
      this.age = age;
    }
  }

}