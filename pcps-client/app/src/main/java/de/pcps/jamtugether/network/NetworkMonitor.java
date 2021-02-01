package de.pcps.jamtugether.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NetworkMonitor extends ConnectivityManager.NetworkCallback {

    @Nullable
    private static NetworkMonitor instance;

    private boolean connectedToInternet;

    public void start(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().build(), this);
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        connectedToInternet = true;
    }

    @Override
    public void onLost(@NonNull Network network) {
        connectedToInternet = false;
    }

    public boolean isConnectedToInternet() {
        return connectedToInternet;
    }

    @NonNull
    public static NetworkMonitor getInstance() {
        if (instance == null) {
            instance = new NetworkMonitor();
        }
        return instance;
    }
}
