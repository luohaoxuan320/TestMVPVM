package com.lehow.testmvp.testpaging;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.lehow.testmvp.R;

/**
 * desc:
 * author: luoh17
 * time: 2018/10/20 19:00
 */
public class MyPagingAdapter extends PagedListAdapter<PageEntity, MyPagingAdapter.MyHolder> {

  protected MyPagingAdapter(@NonNull DiffUtil.ItemCallback diffCallback) {
    super(diffCallback);
  }

  @NonNull @Override public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paging, parent, false);
    return new MyHolder(view);
  }

  @Override public void onBindViewHolder(@NonNull MyPagingAdapter.MyHolder holder, int position) {
    holder.title.setText(
        "Id:" + getItem(position).getId() + " Name=" + getItem(position).getName());
  }

  static class MyHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title) TextView title;

    public MyHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
