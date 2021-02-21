package com.alazar.aggregator.screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alazar.aggregator.R;
import com.alazar.aggregator.adapter.NewsAdapter;
import com.alazar.aggregator.adapter.RecyclerViewClickListener;
import com.alazar.aggregator.base.NetworkProvider;
import com.alazar.aggregator.base.ToastProvider;
import com.alazar.aggregator.databinding.FragmentFeedBinding;
import com.alazar.aggregator.di.App;

import javax.inject.Inject;


public class FeedFragment extends Fragment implements FeedMvpContract.View, RecyclerViewClickListener {

    private FragmentFeedBinding binding;

    private NewsAdapter adapter;

    private int scrollPosition = 0;

    @Inject
    FeedMvpContract.Presenter<FeedMvpContract.View> presenter;

    @Inject
    NetworkProvider networkProvider;
    @Inject
    ToastProvider toastProvider;

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

        initRecyclerView();

        binding.swipeRefresh.setColorSchemeResources(R.color.purple_500);
        binding.swipeRefresh.setOnRefreshListener(() -> {
            if (!networkProvider.isConnected()) {
                toastProvider.makeText(R.string.internet_unavaiable);
                binding.swipeRefresh.setRefreshing(false);
            } else {
                showFeed();
            }
        });

        showFeed();

        return binding.getRoot();
    }

    private void showFeed() {
        presenter.getFeed(newsList -> {
            adapter.setItems(newsList);

            binding.splash.setVisibility(View.INVISIBLE);
            hideProgressBar();
            binding.swipeRefresh.setRefreshing(false);

            recyclerView.scrollToPosition(scrollPosition);
        });
    }

    private void initRecyclerView() {
        recyclerView = binding.recyclerView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new NewsAdapter(this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void recyclerViewListClicked(String link, View v, int position) {

        if (!networkProvider.isConnected()) {
            toastProvider.makeText(R.string.internet_unavaiable);
            return;
        }

        scrollPosition = position;

        launchCustomTab(link);
    }


    private void launchCustomTab(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        builder.setStartAnimations(requireActivity(), R.anim.fragment_fade_enter, R.anim.fragment_fade_exit);
        builder.setExitAnimations(requireActivity(), android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        CustomTabColorSchemeParams params = new CustomTabColorSchemeParams.Builder()
            .setNavigationBarColor(ContextCompat.getColor(requireActivity(), R.color.purple_700))
            .setToolbarColor(ContextCompat.getColor(requireActivity(), R.color.purple_700))
            .setSecondaryToolbarColor(ContextCompat.getColor(requireActivity(), R.color.purple_500))
            .build();
        builder.setColorSchemeParams(CustomTabsIntent.COLOR_SCHEME_LIGHT, params);

        CustomTabsIntent customTabsIntent = builder.build();

        customTabsIntent.launchUrl(requireActivity(), Uri.parse(url));
    }


    private final BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("FeedFragment", "Network state changed");
            if (networkProvider.isConnected()) {
                showProgressBar();

                adapter.clearItems();
                showFeed();
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