package de.pcps.jamtugether.audio.instrument;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;

import de.pcps.jamtugether.audio.soundpool.DrumsSoundPool;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;

public class Drums extends Instrument {

    @Nullable
    private static Drums instance;

    @NonNull
    public static final String PREFERENCE_VALUE = "drums";

    @NonNull
    public static final String SERVER_STRING = "drums";

    public Drums() {
        super(1, R.string.instrument_drums, R.string.play_drums_help, PREFERENCE_VALUE, SERVER_STRING);
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

    @NonNull
    @Override
    public BaseSoundPool createSoundPool(@NonNull Context context) {
        return new DrumsSoundPool(context);
    }

    @NonNull
    public static Drums getInstance() {
        if(instance == null) {
            instance = new Drums();
        }
        return instance;
    }
}
