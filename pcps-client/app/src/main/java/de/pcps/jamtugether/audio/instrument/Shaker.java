package de.pcps.jamtugether.audio.instrument;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.soundpool.ShakerSoundPool;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;

// todo
public class Shaker extends Instrument {

    @Nullable
    private static Shaker instance;

    @NonNull
    public static final String PREFERENCE_VALUE = "shaker";

    @NonNull
    public static final String SERVER_STRING = "shaker";

    public Shaker() {
        super(2, R.string.instrument_shaker, R.string.play_shaker_help, PREFERENCE_VALUE, SERVER_STRING);
    }

    public void shake() {
    }

    @NonNull
    @Override
    public BaseSoundPool createSoundPool(@NonNull Context context) {
        return new ShakerSoundPool(context);
    }

    @NonNull
    public static Shaker getInstance() {
        if(instance == null) {
            instance = new Shaker();
        }
        return instance;
    }
}
