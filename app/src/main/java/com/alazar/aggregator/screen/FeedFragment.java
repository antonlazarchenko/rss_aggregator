package com.alazar.aggregator.screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alazar.aggregator.R;
import com.alazar.aggregator.adapter.NewsAdapter;
import com.alazar.aggregator.adapter.RecyclerViewClickListener;
import com.alazar.aggregator.databinding.FragmentFeedBinding;
import com.alazar.aggregator.di.App;

import javax.inject.Inject;


public class FeedFragment extends Fragment implements FeedMvpContract.View, RecyclerViewClickListener {

    private FragmentFeedBinding binding;

    private NewsAdapter adapter;

    private SwipeRefreshLayout swipeRefresh;

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

        showProgressBar();

        RecyclerView recyclerView = binding.recyclerView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new NewsAdapter(this);
        recyclerView.setAdapter(adapter);

        swipeRefresh = binding.swipeRefresh;
        swipeRefresh.setColorSchemeResources(R.color.purple_500);
        swipeRefresh.setOnRefreshListener(this::updateContent);

        updateContent();

        return binding.getRoot();
    }


    public void updateContent() {
        presenter.getFeed(news -> {
            hideProgressBar();
            swipeRefresh.setRefreshing(false);
            adapter.setItems(news);
        });
    }


    @Override
    public void recyclerViewListClicked(String link, View v, int position) {

        Fragment fragment = new BrowserFragment();

        Bundle bundle = new Bundle();
        bundle.putString("link", link);
        fragment.setArguments(bundle);

        requireActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack("feed")
            .commit();
    }

    @Override
    public void showProgressBar() {
        binding.progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        binding.progress.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        presenter.detachView();
    }
}