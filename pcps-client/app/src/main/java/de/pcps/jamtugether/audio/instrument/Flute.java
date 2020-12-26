package de.pcps.jamtugether.audio.instrument;

import android.content.Context;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.soundpool.FluteSoundPool;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;

@Singleton
public class Flute extends Instrument {

    @NonNull
    public static final String PREFERENCE_VALUE = "flute";

    @NonNull
    public static final String SERVER_STRING = "flute";

    @Inject
    public Flute(@NonNull Context context) {
        super(0, R.string.instrument_flute, R.string.play_flute_help, PREFERENCE_VALUE, SERVER_STRING, context);
    }

    public int play(float pitch) {
        return soundPool.onPlaySoundRes(R.raw.flute_sound, 2, 0);
    }

    @Override
    public BaseSoundPool createSoundPool() {
        return new FluteSoundPool(context);
    }
}
