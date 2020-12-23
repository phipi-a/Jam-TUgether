package de.pcps.jamtugether.model.instrument;

import android.content.Context;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.instrument.base.Instrument;

import de.pcps.jamtugether.model.music.soundpool.DrumsSoundPool;
import de.pcps.jamtugether.model.music.soundpool.base.BaseSoundPool;

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
        soundPool.onPlaySoundRes(R.raw.drum_snare, 1, 0);
    }

    public void playKick() {
        soundPool.onPlaySoundRes(R.raw.drum_kick, 1, 0);
    }

    public void playHat() {
        soundPool.onPlaySoundRes(R.raw.drum_hat, 1, 0);
    }

    public void playCymbal() {
        soundPool.onPlaySoundRes(R.raw.drum_cymbal, 1, 0);
    }

    @Override
    public BaseSoundPool createSoundPool() {
        return new DrumsSoundPool(context);
    }
}
