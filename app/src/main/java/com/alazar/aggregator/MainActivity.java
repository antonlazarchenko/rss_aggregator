package com.alazar.aggregator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alazar.aggregator.base.NetworkProvider;
import com.alazar.aggregator.databinding.ActivityMainBinding;
import com.alazar.aggregator.di.App;
import com.alazar.aggregator.screen.FeedFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    NetworkProvider networkProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        App.getComponent().inject(this);

        checkAndRequestPermissions(Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE
        );

        requestEnableInternet();

        getSupportFragmentManager()
            .beginTransaction()
            .replace(binding.frameLayout.getId(), new FeedFragment())
            .commit();
    }

    private void requestEnableInternet() {

        if (!networkProvider.isConnected()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage(R.string.request_enable_wifi)
                .setPositiveButton(R.string.enable,
                    (dialog, id) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)));

            AlertDialog alert = builder.create();
            alert.show();
        }
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