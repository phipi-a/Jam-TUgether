package de.pcps.jamtugether.audio.instrument;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.soundpool.FluteSoundPool;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;

public class Flute extends Instrument {

    @Nullable
    private static Flute instance;

    @NonNull
    public static final String PREFERENCE_VALUE = "flute";

    @NonNull
    public static final String SERVER_STRING = "flute";

    public Flute() {
        super(0, R.string.instrument_flute, R.string.play_flute_help, PREFERENCE_VALUE, SERVER_STRING);
    }

    public int play(float pitch) {
        return soundPool.onPlaySoundRes(R.raw.flute_sound, 2);
    }

    @NonNull
    @Override
    public BaseSoundPool createSoundPool(@NonNull Context context) {
        return new FluteSoundPool(context);
    }

    @NonNull
    public static Flute getInstance() {
        if(instance == null) {
            instance = new Flute();
        }
        return instance;
    }
}
