package de.pcps.jamtugether.api;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.errors.NoInternetConnectionException;
import de.pcps.jamtugether.utils.NetworkUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class InternetConnectionInterceptor implements Interceptor {

    @Inject
    Context context;

    @Inject
    public InternetConnectionInterceptor() { }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        if(!NetworkUtils.hasInternetConnection(context)) {
            throw new NoInternetConnectionException();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }
}
