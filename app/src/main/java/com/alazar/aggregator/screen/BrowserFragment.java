package com.alazar.aggregator.screen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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


    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentBrowserBinding binding = FragmentBrowserBinding.inflate(inflater, container, false);

        Bundle bundle = requireArguments();
        String link = bundle.getString("link");

        WebView webView = binding.webView;
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("", "onPageFinished");
            }
        });

        webView.loadUrl(link);

        return binding.getRoot();
    }
}