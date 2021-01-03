package de.pcps.jamtugether.audio.instrument.shaker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.sound.pool.ShakerSoundPool;
import de.pcps.jamtugether.model.sound.SoundResource;
import de.pcps.jamtugether.audio.sound.pool.base.BaseSoundPool;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;

// todo
public class Shaker extends Instrument {

    @Nullable
    private static Shaker instance;

    public static int SHAKER_SOUND = SoundResource.SHAKER.getResource();

    public Shaker() {
        super(2, R.string.instrument_shaker, R.string.play_shaker_help, "shaker", "shaker");
    }

    @RawRes
    @Override
    public int getSoundResource(int pitch) {
        // todo
        return SHAKER_SOUND;
    }

    @NonNull
    @Override
    public BaseSoundPool createSoundPool(@NonNull Context context) {
        return new ShakerSoundPool(context);
    }

    @Override
    public boolean soundsNeedToBeStopped() {
        return false;
    }

    @Override
    public boolean soundsNeedToBeResumed() {
        return false;
    }

    @NonNull
    @Override
    public SingleSoundtrack generateSoundtrack(int userID) {
        // todo
        return null;
    }

    @NonNull
    public static Shaker getInstance() {
        if (instance == null) {
            instance = new Shaker();
        }
        return instance;
    }
}
