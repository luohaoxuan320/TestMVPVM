# TestMVPVM

这是一个Rxjava2+Retrofit+Dagger2+ViewModel实现的一个MVPVM测试框架


## Token失效，重新登录

当Token失效时，需要重新登录，很多方案是在BaseActivity中记录打开的Activity列表，或者发广播的方式通知关闭，感觉很low。

当前方案只需两行设置

singleTask+FLAG_ACTIVITY_CLEAR_TASK

登录页 launchMode 为 singleTask
```
    <activity android:name=".LoginActivity" android:launchMode="singleTask" android:label="登录">
```

重新登录的地方
```
  public void btnRelogin(View view) {
    Intent intent = new Intent(this, LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    //由于LoginActivity被 finish了，所以这个没啥用
    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
  }
```
---------------------
## FLAG_ACTIVITY_CLEAR_TASK
如果在调用Context.startActivity时传递这个标记，将会导致任何用来放置该activity的已经存在的task里面的已经存在的activity先清空，然后该activity再在该task中启动，也就是说，这个新启动的activity变为了这个空tas的根activity.所有老的activity都结束掉。该标志必须和FLAG_ACTIVITY_NEW_TASK一起使用。

---------------------

## FLAG_ACTIVITY_CLEAR_TASK黑屏问题

[如何避免使用Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK之后的黑屏问题](https://blog.csdn.net/y505772146/article/details/46800825)


# 弹框提示用户需重新登录

有时候在单点登录，或者token失效的时候，需要弹框显式的提示客户需要重新登录，那么这个弹框怎么处理。

之前封装的NetApi是放在BaseActivity中，所以遇到token失效弹框提示的时候，可以直接用当前的context来创建弹框。但是如果是被踢下线，要怎么弹框提示？？有点尴尬，弹框不能用Application的Context，怎么弄，有一种方案是，使用一个透明的Activity来承载这个弹框。但是这样会带来一个问题，如果app在后台，这样会被直接唤醒打断到了用户，我只想在用户返回的时候才显示，要怎么做？？

后面引入ViewModel后，NetApi的初始化放在了Application中，然后在Application中使用动态代理统一处理token失效的情况，此时又遇到了弹框的问题，application中怎么弹出来？？怎么获取当前栈中栈顶的Activity实例？？

经过搜索有一个类映入了我的眼帘 [ActivityLifecycleCallbacks](https://blog.csdn.net/u010072711/article/details/77090313)

可以在Application中注册这个监听，Activity的生命周期状态都可以感知到。

如是在Application中用一个 WeakReference<Activity> 去持有栈顶的Activity，需要弹框的时候用这个Activity去创建弹框就好了，完美。

刚好最近在研究dagger.android 发现里面对Activty依赖注入简化的逻辑里，也用到了ActivityLifecycleCallbacks，去简化Activity的注入逻辑。
