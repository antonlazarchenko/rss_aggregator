package com.alazar.aggregator.screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
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
import com.alazar.aggregator.util.Networker;
import com.alazar.aggregator.util.Toaster;

import javax.inject.Inject;


public class FeedFragment extends Fragment implements FeedMvpContract.View, RecyclerViewClickListener {

    private FragmentFeedBinding binding;

    private NewsAdapter adapter;

    private SwipeRefreshLayout swipeRefresh;

    private int scrollPosition = 0;

    @Inject
    FeedMvpContract.Presenter<FeedMvpContract.View> presenter;

    private RecyclerView recyclerView;

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

        recyclerView = binding.recyclerView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new NewsAdapter(this);
        recyclerView.setAdapter(adapter);

        swipeRefresh = binding.swipeRefresh;
        swipeRefresh.setColorSchemeResources(R.color.purple_500);
        swipeRefresh.setOnRefreshListener(() -> {
            if (!Networker.getInstance().isConnected()) {
                Toaster.getInstance().makeText(R.string.internet_unavaiable);
                swipeRefresh.setRefreshing(false);
                return;
            }
            presenter.callFeed();
        });

        presenter.getNewsFeed().observe(requireActivity(),
            news -> {
                System.out.println(news);
                adapter.setItems(news);

                binding.splash.setVisibility(View.INVISIBLE);
                hideProgressBar();
                swipeRefresh.setRefreshing(false);

                recyclerView.scrollToPosition(scrollPosition);
            });

        presenter.callFeed();

        return binding.getRoot();
    }


    @Override
    public void recyclerViewListClicked(String link, View v, int position) {

        if (!Networker.getInstance().isConnected()) {
            Toaster.getInstance().makeText(R.string.internet_unavaiable);
            return;
        }

        scrollPosition = position;

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


    private final BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("FeedFragment", "Network state changed");
            if (Networker.getInstance().isConnected()) {
                showProgressBar();

                adapter.clearItems();
                presenter.callFeed();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        requireActivity().registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireActivity().unregisterReceiver(networkChangeReceiver);
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