package de.pcps.jamtugether.di;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.JamTUgetherApplication;
import de.pcps.jamtugether.MainActivity;
import de.pcps.jamtugether.TestingActivity;
import de.pcps.jamtugether.ui.menu.create.CreateRoomViewModel;
import de.pcps.jamtugether.ui.menu.join.JoinRoomViewModel;
import de.pcps.jamtugether.ui.room.RoomViewModel;
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentViewModel;
import de.pcps.jamtugether.ui.room.music.soundtrack.OwnSoundtrackFragment;
import de.pcps.jamtugether.ui.room.music.soundtrack.OwnSoundtrackViewModel;
import de.pcps.jamtugether.ui.room.overview.SoundtrackOverviewFragment;
import de.pcps.jamtugether.ui.room.overview.SoundtrackOverviewViewModel;
import de.pcps.jamtugether.ui.settings.SettingsViewModel;
import de.pcps.jamtugether.ui.welcome.WelcomeViewModel;

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

    public static void inject(@NonNull RoomViewModel roomViewModel) {
        appComponent.inject(roomViewModel);
    }

    public static void inject(@NonNull SoundtrackOverviewFragment soundtrackOverviewFragment) {
        appComponent.inject(soundtrackOverviewFragment);
    }

    public static void inject(@NonNull SoundtrackOverviewViewModel soundtrackOverviewViewModel) {
        appComponent.inject(soundtrackOverviewViewModel);
    }

    public static void inject(@NonNull OwnSoundtrackFragment ownSoundtrackFragment) {
        appComponent.inject(ownSoundtrackFragment);
    }

    public static void inject(@NonNull OwnSoundtrackViewModel ownSoundtrackViewModel) {
        appComponent.inject(ownSoundtrackViewModel);
    }

    public static void inject(@NonNull InstrumentViewModel instrumentViewModel) {
        appComponent.inject(instrumentViewModel);
    }

    public static void inject(@NonNull TestingActivity testingActivity) {
        appComponent.inject(testingActivity);
    }
}


