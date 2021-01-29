package de.pcps.jamtugether.audio.instrument.shaker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.sound.pool.ShakerSoundPool;
import de.pcps.jamtugether.audio.sound.pool.base.BaseSoundPool;
import de.pcps.jamtugether.model.sound.shaker.ShakerSound;

public class Shaker extends Instrument {

    @Nullable
    private static Shaker instance;

    public Shaker() {
        super(2, R.string.instrument_shaker, "shaker", "shaker");
    }

    @RawRes
    @Override
    public int getSoundResource(int pitch) {
        return ShakerSound.SHAKER.getResource();
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

    public void play() {
        if (soundPool != null) {
            soundPool.playSoundRes(ShakerSound.SHAKER.getResource());
        }
    }

    @NonNull
    public static Shaker getInstance() {
        if (instance == null) {
            instance = new Shaker();
        }
        return instance;
    }
}
