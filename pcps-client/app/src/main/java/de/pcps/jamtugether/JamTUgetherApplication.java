package de.pcps.jamtugether;

import android.app.Application;

import de.pcps.jamtugether.base.dagger.AppInjector;
import timber.log.Timber;

public class JamTUgetherApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        AppInjector.buildAppComponent(this);
    }
}
