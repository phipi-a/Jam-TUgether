package de.pcps.jamtugether.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.pcps.jamtugether.JamTUgetherApplication;
import de.pcps.jamtugether.api.ApiConstants;
import de.pcps.jamtugether.api.services.soundtrack.SoundtrackService;
import de.pcps.jamtugether.api.services.room.RoomService;
import de.pcps.jamtugether.storage.Preferences;
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
    public Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(ApiConstants.BASE_URL)
                .build();
    }

    @Singleton
    @Provides
    @NonNull
    public SoundtrackService provideMusicDataService(@NonNull Retrofit retrofit) {
        return retrofit.create(SoundtrackService.class);
    }

    @Singleton
    @Provides
    @NonNull
    public RoomService provideRoomService(@NonNull Retrofit retrofit) {
        return retrofit.create(RoomService.class);
    }
}
