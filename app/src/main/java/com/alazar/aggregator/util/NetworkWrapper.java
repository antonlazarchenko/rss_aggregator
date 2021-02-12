package com.alazar.aggregator.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.alazar.aggregator.R;
import com.alazar.aggregator.di.App;

import javax.inject.Inject;

public class NetworkWrapper {
    @Inject
    Context context;

    private ConnectivityManager connectivityManager;

    private static NetworkWrapper instance;

    public static NetworkWrapper getInstance() {
        if (instance == null) {
            instance = new NetworkWrapper();
        }
        return instance;
    }

    protected NetworkWrapper () {
        App.getComponent().inject(this);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public boolean isConnected() {
        boolean isConnected;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = connectivityManager.getActiveNetwork();
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);

            isConnected = actNw != null
                && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        } else {
            NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
            isConnected = nwInfo != null && nwInfo.isConnected();
        }

        return isConnected;
    }

    public void requestEnableInternet() {
        if (!isConnected()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setMessage(R.string.request_enable_wifi)
                .setPositiveButton(R.string.enable,
                    (dialog, id) -> context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                .setNegativeButton(R.string.cancel, (dialog, id) -> ((Activity) context).finish());

            AlertDialog alert = builder.create();
            alert.show();
        }
    }


    public void runNetworkConnectionMonitor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(@NonNull Network network) {
                    super.onAvailable(network);
                }

                @Override
                public void onLost(@NonNull Network network) {
                    super.onLost(network);
                    Toast.makeText(context, context.getString(R.string.internet_lost), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onUnavailable() {
                    super.onUnavailable();
                    Toast.makeText(context, context.getString(R.string.internet_unavaiable), Toast.LENGTH_LONG).show();
                }
            });

        }
    }
}
