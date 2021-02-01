package de.pcps.jamtugether.network;

import androidx.annotation.NonNull;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class NetworkConnectionInterceptor implements Interceptor {

    @Inject
    public NetworkConnectionInterceptor() { }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        if (!NetworkManager.getInstance().isConnectedToInternet()) {
            throw new NoNetworkConnectionException();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }
}
