package com.lehow.testmvp.testpaging;

import android.arch.paging.PagedList;
import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.AsyncDifferConfig;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.lehow.testmvp.R;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * desc:
 * author: luoh17
 * time: 2018/10/20 19:00
 */
public class MyPagingAdapter extends PagedListAdapter<PageEntity, MyPagingAdapter.MyHolder> {

  final int vt_item = 0;
  final int vt_footer = 1;
  private int curLmState = PagingViewModel.state_finish;
  private PagingViewModel pagingViewModel;
  protected MyPagingAdapter(@NonNull DiffUtil.ItemCallback diffCallback) {
    super(diffCallback);
  }

  @NonNull @Override public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paging, parent, false);
    return new MyHolder(view);
  }

  @Override public void onBindViewHolder(@NonNull MyPagingAdapter.MyHolder holder, int position) {
    holder.itemView.setOnClickListener(null);
    if (getItemViewType(position) == vt_footer) {
      if (curLmState == PagingViewModel.state_retry) {
        holder.title.setText("加载失败，请重试");
        if (pagingViewModel.retrySubject != null) {

          holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
              pagingViewModel.retrySubject.subscribe();
            }
          });
        }
      } else {
        holder.title.setText("加载中，请稍后");
      }
    } else {
      holder.title.setText("position:" + position + " Name=" + getItem(position).getName());
    }
  }

  @Override public int getItemCount() {
    return super.getItemCount() + (curLmState == PagingViewModel.state_finish ? 0 : 1);
  }

  @Override public int getItemViewType(int position) {
    if (curLmState != PagingViewModel.state_finish && position == getItemCount() - 1) {
      return vt_footer;
    } else {
      return vt_item;
    }
  }

  public void updateLoadMoreState(int lmState) {
    Log.i("TAG", "updateLoadMoreState: " + lmState);
    int preState = curLmState;

    curLmState = lmState;
    if (lmState == PagingViewModel.state_finish) {
      notifyItemRemoved(super.getItemCount());
    } else if (lmState == PagingViewModel.state_retry) {
      notifyItemChanged(super.getItemCount());
    } else {
      Log.i("TAG", "updateLoadMoreState: "
          + lmState
          + " getItemCount="
          + super.getItemCount()
          + " cur="
          + getItemCount());
      if (preState == PagingViewModel.state_retry) {
        notifyItemChanged(super.getItemCount());
      } else {
        notifyItemInserted(super.getItemCount());
      }
    }
  }

  public void bindViewModel(PagingViewModel pagingViewModel) {
    this.pagingViewModel = pagingViewModel;
    pagingViewModel.pagedListObservable.subscribe(new Consumer<PagedList<PageEntity>>() {
      @Override public void accept(PagedList<PageEntity> pageEntities) throws Exception {
        for (int i = 0; i < pageEntities.size(); i++) {
          Log.i("TAG",
              "accept: entity id=" + pageEntities.get(i).getId() + " name=" + pageEntities.get(i)
                  .getName());
        }
        submitList(pageEntities);
      }
    }, new Consumer<Throwable>() {
      @Override public void accept(Throwable throwable) throws Exception {
        Log.i("TAG", "accept: " + throwable);
      }
    });

    pagingViewModel.loadMoreState.observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Integer>() {
          @Override public void accept(Integer integer) throws Exception {
            updateLoadMoreState(integer);
          }
        });
  }
  static class MyHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title) TextView title;

    public MyHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
