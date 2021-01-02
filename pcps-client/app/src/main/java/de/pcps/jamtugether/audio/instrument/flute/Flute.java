package de.pcps.jamtugether.audio.instrument.flute;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.sound.OnSoundPlayedCallback;
import de.pcps.jamtugether.audio.sound.pool.FluteSoundPool;
import de.pcps.jamtugether.audio.sound.SoundResource;
import de.pcps.jamtugether.audio.sound.pool.base.BaseSoundPool;
import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.utils.TimeUtils;

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

    @Override
    public boolean soundsNeedToBeResumed() {
        return true;
    }

    @NonNull
    @Override
    public SingleSoundtrack generateSoundtrack(int userID) {
        Random random = new Random();
        List<Sound> soundSequence = new ArrayList<>();
        for (int j = 0; j < 20; j++) {
            int pitch = random.nextInt(80) + 20;
            soundSequence.add(new Sound(getServerString(), 0, (int) TimeUtils.ONE_SECOND * j, (int) TimeUtils.ONE_SECOND * (j + 1), pitch));
        }
        return new SingleSoundtrack(userID, soundSequence);
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
