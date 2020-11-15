package de.pcps.jamtugether.dagger;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import de.pcps.jamtugether.MyApplication;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public class AppModule {

    public static final String BASE_URL = ""; // todo add base url

    @NonNull
    private final MyApplication application;

    public AppModule(@NonNull MyApplication application) {
        this.application = application;
    }

    @Provides
    public Context provideContext() {
        return application;
    }

    @Provides
    public Application provideApplication() {
        return application;
    }


    @Provides
    public Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }
}
