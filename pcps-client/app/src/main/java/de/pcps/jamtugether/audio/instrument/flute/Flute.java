package de.pcps.jamtugether.audio.instrument.flute;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.sound.OnSoundPlayedCallback;
import de.pcps.jamtugether.audio.sound.BaseSoundPool;

public class Flute extends Instrument {

    public static final int MIN_PITCH = 0;
    public static final int MAX_PITCH = 100;
    public static final int PITCH_RANGE = MAX_PITCH - MIN_PITCH;

    @Nullable
    private static Flute instance;

    public Flute() {
        super(0, R.string.instrument_flute, "flute", "flute");
    }

    @RawRes
    @Override
    public int getSoundResource(int pitch) {
        return FluteSound.from(pitch).getResource();
    }

    @NonNull
    @Override
    public BaseSoundPool createSoundPool(@NonNull Context context) {
        return new FluteSoundPool(context);
    }

    @Override
    public boolean soundsNeedToBeStopped() {
        return true;
    }

    @Override
    public boolean soundsNeedToBeResumed() {
        return true;
    }

    public void play(int pitch, @NonNull OnSoundPlayedCallback callback) {
        if (soundPool != null) {
            soundPool.playSoundRes(getSoundResource(pitch), callback);
        } else {
            callback.onSoundPlayed(0);
        }
    }

    @NonNull
    public static Flute getInstance() {
        if (instance == null) {
            instance = new Flute();
        }
        return instance;
    }
}
