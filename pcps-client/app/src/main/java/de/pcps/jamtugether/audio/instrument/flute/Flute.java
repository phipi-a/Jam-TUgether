package de.pcps.jamtugether.audio.instrument.flute;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.soundpool.FluteSoundPool;
import de.pcps.jamtugether.audio.SoundResource;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;

public class Flute extends Instrument {

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
    public int getSoundResource(int element) {
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

    @NonNull
    @Override
    public SingleSoundtrack generateSoundtrack(int userID) {
        return null;
    }

    public int play(float pitch) {
        return soundPool.playSoundRes(FLUTE_SOUND, pitch);
    }

    @NonNull
    public static Flute getInstance() {
        if (instance == null) {
            instance = new Flute();
        }
        return instance;
    }
}
