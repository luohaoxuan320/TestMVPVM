package com.lehow.testmvp.testpaging;

import android.support.v7.util.DiffUtil;

/**
 * desc:
 * author: luoh17
 * time: 2018/10/20 19:38
 */
public class MyDifferUtil extends DiffUtil.ItemCallback<PageEntity> {
  @Override public boolean areItemsTheSame(PageEntity oldItem, PageEntity newItem) {
    return oldItem.getId() == newItem.getId();
  }

  @Override public boolean areContentsTheSame(PageEntity oldItem, PageEntity newItem) {
    return oldItem.getName().equals(newItem.getName());
  }
}
