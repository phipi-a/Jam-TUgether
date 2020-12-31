package de.pcps.jamtugether;

import android.app.Application;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.base.Instruments;
import de.pcps.jamtugether.audio.SoundResource;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.log.JamTimberTree;
import de.pcps.jamtugether.utils.SoundUtils;
import timber.log.Timber;

public class JamTUgetherApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppInjector.buildAppComponent(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new JamTimberTree());
        }

        for (Instrument instrument : Instruments.LIST) {
            instrument.loadSounds(this.getApplicationContext());
        }

        for(SoundResource soundResource : SoundResource.values()) {
            int duration = SoundUtils.getSoundDuration(soundResource.getResource(), this.getApplicationContext());
            soundResource.setDuration(duration);
        }
    }
}
