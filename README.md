# TestMVPVM


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
