package de.pcps.jamtugether.audio.instrument;

import android.content.Context;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;

import de.pcps.jamtugether.audio.soundpool.DrumsSoundPool;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;

@Singleton
public class Drums extends Instrument {

    @NonNull
    public static final String PREFERENCE_VALUE = "drums";

    @NonNull
    public static final String SERVER_STRING = "drums";

    @Inject
    public Drums(@NonNull Context context) {
        super(1, R.string.instrument_drums, R.string.play_drums_help, PREFERENCE_VALUE, SERVER_STRING, context);
    }

    public void playSnare() {
        soundPool.onPlaySoundRes(R.raw.drum_snare, 1);
    }

    public void playKick() {
        soundPool.onPlaySoundRes(R.raw.drum_kick, 1);
    }

    public void playHat() {
        soundPool.onPlaySoundRes(R.raw.drum_hat, 1);
    }

    public void playCymbal() {
        soundPool.onPlaySoundRes(R.raw.drum_cymbal, 1);
    }

    @Override
    public BaseSoundPool createSoundPool() {
        return new DrumsSoundPool(context);
    }
}
