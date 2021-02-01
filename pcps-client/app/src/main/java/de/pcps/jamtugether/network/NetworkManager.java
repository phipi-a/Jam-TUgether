package de.pcps.jamtugether.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NetworkManager extends ConnectivityManager.NetworkCallback {

    @Nullable
    private static NetworkManager instance;

    private boolean connectedToInternet;

    public void observeNetworks(@NonNull Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET);
        NetworkRequest request = builder.build();
        connectivityManager.registerNetworkCallback(request, this);
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        connectedToInternet = true;
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        connectedToInternet = false;
    }

    public boolean isConnectedToInternet() {
        return connectedToInternet;
    }

    @NonNull
    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }
}
