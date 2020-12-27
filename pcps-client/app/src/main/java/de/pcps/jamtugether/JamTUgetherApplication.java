package de.pcps.jamtugether;

import android.app.Application;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.base.Instruments;
import de.pcps.jamtugether.di.AppInjector;
import timber.log.Timber;

public class JamTUgetherApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppInjector.buildAppComponent(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        for (Instrument instrument : Instruments.LIST) {
            instrument.loadSounds(this.getApplicationContext());
        }
    }
}
