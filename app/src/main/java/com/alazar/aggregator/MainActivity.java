package com.alazar.aggregator;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alazar.aggregator.databinding.ActivityMainBinding;
import com.alazar.aggregator.screen.FeedFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        checkAndRequestPermissions(Manifest.permission.INTERNET);

        getSupportFragmentManager()
            .beginTransaction()
            .replace(binding.frameLayout.getId(), new FeedFragment())
            .commit();
    }


    protected void checkAndRequestPermissions(String... permissions) {
        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(perm);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            //noinspection ToArrayCallWithZeroLengthArrayArgument
            ActivityCompat.requestPermissions(
                this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 123);
        }
    }
}