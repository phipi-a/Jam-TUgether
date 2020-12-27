package de.pcps.jamtugether.audio.instrument;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.soundpool.FluteSoundPool;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;

public class Flute extends Instrument {

    @Nullable
    private static Flute instance;

    @RawRes
    private static final int FLUTE = R.raw.flute_sound;

    public Flute() {
        super(0, R.string.instrument_flute, R.string.play_flute_help, "flute", "flute");
    }

    @RawRes
    @Override
    public int getSoundResource(int element) {
        return FLUTE;
    }

    @NonNull
    @Override
    public BaseSoundPool createSoundPool(@NonNull Context context) {
        return new FluteSoundPool(context);
    }

    public int play(float pitch) {
        return soundPool.playSoundRes(FLUTE, 2);
    }

    @NonNull
    public static Flute getInstance() {
        if (instance == null) {
            instance = new Flute();
        }
        return instance;
    }
}
