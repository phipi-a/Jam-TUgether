package de.pcps.jamtugether.audio.instrument.shaker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.soundpool.ShakerSoundPool;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;

// todo
public class Shaker extends Instrument {

    @Nullable
    private static Shaker instance;

    public Shaker() {
        super(2, R.string.instrument_shaker, R.string.play_shaker_help, "shaker", "shaker");
    }

    @RawRes
    @Override
    public int getSoundResource(int element) {
        // todo
        return R.raw.flute_sound;
    }

    @NonNull
    @Override
    public BaseSoundPool createSoundPool(@NonNull Context context) {
        return new ShakerSoundPool(context);
    }

    @NonNull
    public static Shaker getInstance() {
        if (instance == null) {
            instance = new Shaker();
        }
        return instance;
    }
}
