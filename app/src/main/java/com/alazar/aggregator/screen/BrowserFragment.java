package com.alazar.aggregator.screen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alazar.aggregator.databinding.FragmentBrowserBinding;


public class BrowserFragment extends Fragment {

    public BrowserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentBrowserBinding binding = FragmentBrowserBinding.inflate(inflater, container, false);

        Bundle bundle = requireArguments();

        WebView webView = binding.webView;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(bundle.getString("link"));

        return binding.getRoot();
    }
}