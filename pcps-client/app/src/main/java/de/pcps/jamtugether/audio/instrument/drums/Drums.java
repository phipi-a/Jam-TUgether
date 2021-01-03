package de.pcps.jamtugether.audio.instrument.drums;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;

import de.pcps.jamtugether.audio.sound.pool.DrumsSoundPool;
import de.pcps.jamtugether.model.sound.SoundResource;
import de.pcps.jamtugether.audio.sound.pool.base.BaseSoundPool;
import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.utils.TimeUtils;

public class Drums extends Instrument {

    @Nullable
    private static Drums instance;

    @RawRes
    public static final int SNARE = SoundResource.SNARE.getResource();
    public static final int SNARE_PITCH = 50;

    @RawRes
    public static final int KICK = SoundResource.KICK.getResource();
    public static final int KICK_PITCH = 30;

    @RawRes
    public static final int HAT = SoundResource.HAT.getResource();
    public static final int HAT_PITCH = 70;

    @RawRes
    public static final int CYMBAL = SoundResource.CYMBAL.getResource();
    public static final int CYMBAL_PITCH = 90;

    public Drums() {
        super(1, R.string.instrument_drums, R.string.play_drums_help, "drums", "drums");
    }

    @RawRes
    @Override
    public int getSoundResource(int pitch) {
        switch (pitch) {
            case SNARE_PITCH:
                return SNARE;
            case KICK_PITCH:
                return KICK;
            case HAT_PITCH:
                return HAT;
            default:
                return CYMBAL;
        }
    }

    @NonNull
    @Override
    public BaseSoundPool createSoundPool(@NonNull Context context) {
        return new DrumsSoundPool(context);
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
        Random random = new Random();
        List<Sound> soundSequence = new ArrayList<>();
        for (int j = 0; j < 20; j++) {
            int[] array = {Drums.SNARE_PITCH, Drums.KICK_PITCH, Drums.HAT_PITCH, Drums.CYMBAL_PITCH};
            int pitch = array[random.nextInt(array.length)];
            soundSequence.add(new Sound((int) TimeUtils.ONE_SECOND * j, (int) TimeUtils.ONE_SECOND * (j + 1), pitch));
        }
        return new SingleSoundtrack(userID, getServerString(), soundSequence);
    }

    public void playSnare() {
        if (soundPool != null) {
            soundPool.playSoundRes(SNARE, 1);
        }
    }

    public void playKick() {
        if (soundPool != null) {
            soundPool.playSoundRes(KICK, 1);
        }
    }

    public void playHat() {
        if (soundPool != null) {
            soundPool.playSoundRes(HAT, 1);
        }
    }

    public void playCymbal() {
        if (soundPool != null) {
            soundPool.playSoundRes(CYMBAL, 1);
        }
    }

    @NonNull
    public static Drums getInstance() {
        if (instance == null) {
            instance = new Drums();
        }
        return instance;
    }
}
