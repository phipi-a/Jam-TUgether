package de.pcps.jamtugether.base.dagger;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import dagger.Module;
import dagger.Provides;
import de.pcps.jamtugether.JamTUgetherApplication;
import de.pcps.jamtugether.base.communication.Constants;
import de.pcps.jamtugether.base.communication.api.MusicDataService;
import de.pcps.jamtugether.base.communication.api.RoomService;
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

    @Provides
    @NonNull
    public SharedPreferences provideSharedPreferences(@NonNull Context context) {
        return context.getSharedPreferences(Preferences.FILE_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    @NonNull
    public Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build();
    }

    @Provides
    @NonNull
    public MusicDataService provideMusicDataService(@NonNull Retrofit retrofit) {
        return retrofit.create(MusicDataService.class);
    }

    @Provides
    @NonNull
    public RoomService provideRoomService(@NonNull Retrofit retrofit) {
        return retrofit.create(RoomService.class);
    }
}
