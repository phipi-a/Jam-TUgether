package de.pcps.jamtugether.base.dagger;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.JamTUgetherApplication;
import de.pcps.jamtugether.MainActivity;
import de.pcps.jamtugether.content.main_instrument.MainInstrumentFragment;
import de.pcps.jamtugether.content.main_instrument.MainInstrumentViewModel;

public class AppInjector {

    private static AppComponent appComponent;

    public static void buildAppComponent(@NonNull JamTUgetherApplication application) {
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(application)).build();
    }

    public static void inject(@NonNull MainActivity mainActivity) {
        appComponent.inject(mainActivity);
    }

    public static void inject(@NonNull MainInstrumentFragment mainInstrumentFragment) {
        appComponent.inject(mainInstrumentFragment);
    }

    public static void inject(@NonNull MainInstrumentViewModel mainInstrumentViewModel) {
        appComponent.inject(mainInstrumentViewModel);
    }
}
