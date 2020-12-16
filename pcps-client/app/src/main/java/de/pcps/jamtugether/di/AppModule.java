package de.pcps.jamtugether.di;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.pcps.jamtugether.JamTUgetherApplication;
import de.pcps.jamtugether.api.Constants;
import de.pcps.jamtugether.api.interceptors.InternetConnectionInterceptor;
import de.pcps.jamtugether.api.services.soundtrack.SoundtrackService;
import de.pcps.jamtugether.api.services.room.RoomService;
import de.pcps.jamtugether.storage.Preferences;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public class AppModule {

    @NonNull
    private final JamTUgetherApplication application;

    public AppModule(@NonNull JamTUgetherApplication application) {
        this.application = application;
    }

    @Provides
    @NonNull
    public Context provideContext() {
        return application.getApplicationContext();
    }

    @Provides
    @NonNull
    public Application provideApplication() {
        return application;
    }

    @Singleton
    @Provides
    @NonNull
    public SharedPreferences provideSharedPreferences(@NonNull Context context) {
        return context.getSharedPreferences(Preferences.FILE_NAME, Context.MODE_PRIVATE);
    }

    @Singleton
    @Provides
    @NonNull
    public Retrofit provideRetrofit(@NonNull InternetConnectionInterceptor interceptor) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor).build();

        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build();
    }

    @Singleton
    @Provides
    @NonNull
    public SoundtrackService provideSoundtrackService(@NonNull Retrofit retrofit) {
        return retrofit.create(SoundtrackService.class);
    }

    @Singleton
    @Provides
    @NonNull
    public RoomService provideRoomService(@NonNull Retrofit retrofit) {
        return retrofit.create(RoomService.class);
    }
}