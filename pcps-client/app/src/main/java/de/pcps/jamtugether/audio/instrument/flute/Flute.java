package de.pcps.jamtugether.audio.instrument.flute;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.sound.OnSoundPlayedCallback;
import de.pcps.jamtugether.audio.sound.pool.FluteSoundPool;
import de.pcps.jamtugether.model.sound.SoundResource;
import de.pcps.jamtugether.audio.sound.pool.base.BaseSoundPool;

public class Flute extends Instrument {

    public static final int MIN_PITCH = 20;
    public static final int MAX_PITCH = 100;
    public static final int PITCH_RANGE = MAX_PITCH - MIN_PITCH;

    public static final float PITCH_MIN_PERCENTAGE = 0.2f;
    public static final float PITCH_MAX_PERCENTAGE = 1f;
    public static final float PITCH_MULTIPLIER = 3f;
    public static final float PITCH_DEFAULT_PERCENTAGE = 0.3f;

    @RawRes
    public static int FLUTE_SOUND = SoundResource.FLUTE.getResource();

    @Nullable
    private static Flute instance;

    public Flute() {
        super(0, R.string.instrument_flute, R.string.play_flute_help, "flute", "flute");
    }

    @RawRes
    @Override
    public int getSoundResource(int pitch) {
        return FLUTE_SOUND;
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

    public void play(float pitch, @NonNull OnSoundPlayedCallback callback) {
        if (soundPool != null) {
            soundPool.playSoundRes(FLUTE_SOUND, pitch, callback);
        }
        callback.onSoundPlayed(0);
    }

    @NonNull
    public static Flute getInstance() {
        if (instance == null) {
            instance = new Flute();
        }
        return instance;
    }
}
