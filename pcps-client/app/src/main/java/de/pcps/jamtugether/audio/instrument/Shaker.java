package de.pcps.jamtugether.audio.instrument;

import android.content.Context;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.soundpool.FluteSoundPool;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;

// todo
@Singleton
public class Shaker extends Instrument {

    @NonNull
    public static final String PREFERENCE_VALUE = "shaker";

    @NonNull
    public static final String SERVER_STRING = "shaker";

    @Inject
    public Shaker(@NonNull Context context) {
        super(2, R.string.instrument_shaker, R.string.play_shaker_help, PREFERENCE_VALUE, SERVER_STRING, context);
    }

    public void shake() {
    }

    @Override
    public BaseSoundPool createSoundPool() {
        return new FluteSoundPool(context);
    }
}
