package de.pcps.jamtugether.base.dagger;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.JamTUgetherApplication;
import de.pcps.jamtugether.MainActivity;
import de.pcps.jamtugether.content.room.create.CreateRoomFragment;
import de.pcps.jamtugether.content.room.join.JoinRoomFragment;
import de.pcps.jamtugether.content.welcome.WelcomeFragment;
import de.pcps.jamtugether.content.welcome.WelcomeViewModel;
import de.pcps.jamtugether.content.menu.MenuFragment;

public class AppInjector {

    private static AppComponent appComponent;

    public static void buildAppComponent(@NonNull JamTUgetherApplication application) {
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(application)).build();
    }

    public static void inject(@NonNull MainActivity mainActivity) {
        appComponent.inject(mainActivity);
    }

    public static void inject(@NonNull WelcomeFragment welcomeFragment) {
        appComponent.inject(welcomeFragment);
    }

    public static void inject(@NonNull WelcomeViewModel welcomeViewModel) {
        appComponent.inject(welcomeViewModel);
    }

    public static void inject(@NonNull MenuFragment menuFragment) {
        appComponent.inject(menuFragment);
    }

    public static void inject(@NonNull CreateRoomFragment createRoomFragment) {
        appComponent.inject(createRoomFragment);
    }

    public static void inject(@NonNull JoinRoomFragment joinRoomFragment) {
        appComponent.inject(joinRoomFragment);
    }
}


