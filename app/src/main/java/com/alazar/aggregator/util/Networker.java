package com.alazar.aggregator.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.alazar.aggregator.R;
import com.alazar.aggregator.base.NetworkProvider;
import com.alazar.aggregator.di.App;

import javax.inject.Inject;

public class Networker implements NetworkProvider {
    @Inject
    Context context;

    private final ConnectivityManager connectivityManager;

    @Inject
    public Networker() {
        App.getComponent().inject(this);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public boolean isConnected() {
        boolean isConnected;

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


    public void runNetworkConnectionMonitor() {

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
            }

            @Override
            public void onUnavailable() {
                super.onUnavailable();
                Toast.makeText(context, context.getString(R.string.internet_unavaiable), Toast.LENGTH_LONG);
            }

            @Override
            public void onLost(Network network) {
                Toast.makeText(context, context.getString(R.string.internet_lost), Toast.LENGTH_LONG).show();
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else {
            NetworkRequest request = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
            connectivityManager.registerNetworkCallback(request, networkCallback);
        }
    }
}
