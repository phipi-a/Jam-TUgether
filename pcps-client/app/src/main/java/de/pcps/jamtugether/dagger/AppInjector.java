package de.pcps.jamtugether.dagger;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.JamTUgetherApplication;
import de.pcps.jamtugether.MainActivity;
import de.pcps.jamtugether.content.menu.create.CreateRoomViewModel;
import de.pcps.jamtugether.content.menu.join.JoinRoomViewModel;
import de.pcps.jamtugether.content.room.music.soundtrack.SoundtrackViewModel;
import de.pcps.jamtugether.content.room.overview.RoomOverviewViewModel;
import de.pcps.jamtugether.content.settings.SettingsViewModel;
import de.pcps.jamtugether.content.welcome.WelcomeViewModel;

public class AppInjector {

    private static AppComponent appComponent;

    public static void buildAppComponent(@NonNull JamTUgetherApplication application) {
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(application)).build();
    }

    public static void inject(@NonNull MainActivity mainActivity) {
        appComponent.inject(mainActivity);
    }

    public static void inject(@NonNull WelcomeViewModel welcomeViewModel) {
        appComponent.inject(welcomeViewModel);
    }

    public static void inject(@NonNull SettingsViewModel settingsViewModel) {
        appComponent.inject(settingsViewModel);
    }

    public static void inject(@NonNull CreateRoomViewModel createRoomViewModel) {
        appComponent.inject(createRoomViewModel);
    }

    public static void inject(@NonNull JoinRoomViewModel joinRoomViewModel) {
        appComponent.inject(joinRoomViewModel);
    }

    public static void inject(@NonNull RoomOverviewViewModel roomOverviewViewModel) {
        appComponent.inject(roomOverviewViewModel);
    }

    public static void inject(@NonNull SoundtrackViewModel soundtrackViewModel) {
        appComponent.inject(soundtrackViewModel);
    }
}


