package com.lehow.testmvp.testfrg;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lehow.testmvp.R;
import com.lehow.testmvp.detail.DetailViewModel;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import io.reactivex.functions.Consumer;

public class BlankFragment extends Fragment {

  public BlankFragment() {
    // Required empty public constructor
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.i("TAG", "onCreate: " + ViewModelProviders.of(this).get(DetailViewModel.class));
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_blank, container, false);
  }

  @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    Log.i("TAG", "onActivityCreated: " + ViewModelProviders.of(this).get(DetailViewModel.class));
    Log.i("TAG", "onActivityCreated: getActivity=" + ViewModelProviders.of(getActivity())
        .get(DetailViewModel.class));
  }

  @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Log.i("TAG", "onViewCreated: " + ViewModelProviders.of(this).get(DetailViewModel.class));

    view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ViewModelProviders.of(BlankFragment.this)
            .get(DetailViewModel.class)
            .getValueInfo()
            .as(AutoDispose.<Long>autoDisposable(
                AndroidLifecycleScopeProvider.from(getLifecycle())))
            .subscribe(new Consumer<Long>() {
              @Override public void accept(Long aLong) throws Exception {
                System.out.println("accept=" + aLong);
              }
            });
      }
    });
  }

  @Override public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override public void onDetach() {
    super.onDetach();
  }
}
