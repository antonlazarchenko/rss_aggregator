package com.alazar.aggregator.screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alazar.aggregator.adapter.NewsAdapter;
import com.alazar.aggregator.adapter.RecyclerViewClickListener;
import com.alazar.aggregator.databinding.FragmentFeedBinding;
import com.alazar.aggregator.di.App;

import javax.inject.Inject;


public class FeedFragment extends Fragment implements FeedMvpContract.View, RecyclerViewClickListener {



    private FragmentFeedBinding binding;

    @Inject
    FeedMvpContract.Presenter<FeedMvpContract.View> presenter;

    public FeedFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFeedBinding.inflate(inflater, container, false);
        App.getComponent().inject(this);
        presenter.attachView(this);

        RecyclerView recyclerView = binding.recyclerView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        NewsAdapter adapter = new NewsAdapter(this);
        recyclerView.setAdapter(adapter);

        presenter.getFeed(adapter::setItems);

        return binding.getRoot();
    }


    @Override
    public void recyclerViewListClicked(View v, int position) {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        presenter.detachView();
    }
}