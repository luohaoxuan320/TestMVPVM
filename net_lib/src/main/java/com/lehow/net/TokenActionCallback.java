package com.lehow.net;

import io.reactivex.Maybe;

/**
 * desc:
 * author: luoh17
 * time: 2018/11/8 11:46
 */
public interface TokenActionCallback<R> {

  Maybe<R> getUpdateTokenMaybe();

  void onUpdateTokenSuccess(R tokenResult);
}
