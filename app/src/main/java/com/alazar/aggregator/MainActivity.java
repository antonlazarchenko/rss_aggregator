package com.alazar.aggregator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.alazar.aggregator.adapter.NewsAdapter;
import com.alazar.aggregator.adapter.RecyclerViewClickListener;
import com.alazar.aggregator.databinding.ActivityMainBinding;
import com.alazar.aggregator.rss.RssService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkAndRequestPermissions(Manifest.permission.INTERNET);

        RecyclerView recyclerView = binding.recyclerView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        NewsAdapter adapter = new NewsAdapter(this);
        recyclerView.setAdapter(adapter);

        RssService.getInstance().getFeed(newsList -> {
            System.out.println("SIZE ----- " + newsList.size());
            adapter.setItems(newsList);
        });

    }


    protected void checkAndRequestPermissions(String... permissions) {
        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(perm);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 123);
        }
    }

    @Override
    public void recyclerViewListClicked(View v, int position) {

    }
}