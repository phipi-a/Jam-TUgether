package de.pcps.jamtugether;

import android.app.Application;

import de.pcps.jamtugether.dagger.AppInjector;
import timber.log.Timber;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        AppInjector.buildAppComponent(this);
    }
}
