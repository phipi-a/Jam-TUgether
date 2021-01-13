package de.pcps.jamtugether.audio.instrument.shaker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.sound.pool.ShakerSoundPool;
import de.pcps.jamtugether.audio.sound.pool.base.InstrumentSoundPool;
import de.pcps.jamtugether.model.sound.SoundResource;

public class Shaker extends Instrument {

    @Nullable
    private static Shaker instance;

    public static int SHAKER_SOUND = SoundResource.SHAKER.getResource();

    public Shaker() {
        super(2, R.string.instrument_shaker, R.string.play_shaker_help, "shaker", "shaker");
    }

    @RawRes
    @Override
    public int getSoundResource(int element) {
        return SHAKER_SOUND;
    }

    @NonNull
    @Override
    public InstrumentSoundPool createSoundPool(@NonNull Context context) {
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
            soundPool.playSoundRes(SHAKER_SOUND, 1);
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
