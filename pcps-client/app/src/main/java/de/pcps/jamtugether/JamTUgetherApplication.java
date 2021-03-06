package de.pcps.jamtugether;

import android.app.Application;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.base.Instruments;
import de.pcps.jamtugether.audio.metronome.Metronome;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.log.JamTimberTree;
import de.pcps.jamtugether.audio.instrument.drums.DrumsSound;
import de.pcps.jamtugether.audio.instrument.flute.FluteSound;
import de.pcps.jamtugether.audio.metronome.MetronomeSound;
import de.pcps.jamtugether.audio.instrument.piano.PianoSound;
import de.pcps.jamtugether.audio.instrument.shaker.ShakerSound;
import de.pcps.jamtugether.network.NetworkMonitor;
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

        NetworkMonitor.getInstance().start(this.getApplicationContext());

        for (Instrument instrument : Instruments.LIST) {
            instrument.loadSounds(this.getApplicationContext());
        }

        Metronome.getInstance().loadSounds(this.getApplicationContext());

        for (FluteSound soundResource : FluteSound.values()) {
            int duration = SoundUtils.getSoundDuration(soundResource.getResource(), this.getApplicationContext());
            soundResource.setDuration(duration);
        }

        for (DrumsSound soundResource : DrumsSound.values()) {
            int duration = SoundUtils.getSoundDuration(soundResource.getResource(), this.getApplicationContext());
            soundResource.setDuration(duration);
        }

        for (ShakerSound soundResource : ShakerSound.values()) {
            int duration = SoundUtils.getSoundDuration(soundResource.getResource(), this.getApplicationContext());
            soundResource.setDuration(duration);
        }

        for (PianoSound soundResource : PianoSound.values()) {
            int duration = SoundUtils.getSoundDuration(soundResource.getResource(), this.getApplicationContext());
            soundResource.setDuration(duration);
        }

        for (MetronomeSound soundResource : MetronomeSound.values()) {
            int duration = SoundUtils.getSoundDuration(soundResource.getResource(), this.getApplicationContext());
            soundResource.setDuration(duration);
        }
    }
}
