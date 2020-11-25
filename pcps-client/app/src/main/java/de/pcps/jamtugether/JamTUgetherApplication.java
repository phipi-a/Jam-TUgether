package de.pcps.jamtugether;

import android.app.Application;

import de.pcps.jamtugether.dagger.AppInjector;
import timber.log.Timber;

public class JamTUgetherApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppInjector.buildAppComponent(this);
        Timber.plant(new Timber.DebugTree());
    }
}
