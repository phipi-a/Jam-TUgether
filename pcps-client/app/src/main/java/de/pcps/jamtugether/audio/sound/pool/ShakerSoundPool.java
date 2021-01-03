package de.pcps.jamtugether.audio.sound.pool;

import android.content.Context;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.audio.instrument.shaker.Shaker;
import de.pcps.jamtugether.audio.sound.pool.base.BaseSoundPool;

// todo
public class ShakerSoundPool extends BaseSoundPool {
    private static final int SOUND_POOL_MAX_STREAMS = 100;
    public ShakerSoundPool(@NonNull Context context) {
        super(context, SOUND_POOL_MAX_STREAMS, Shaker.SHAKER_SOUND);
    }

    @Override
    public int play(int soundID, float pitch) {
        return soundPool.play(soundID, volume, volume, 0, 0, calculatePitch((int) pitch));
    }

    @Override
    public float calculatePitch(int pitchPercentage) {
        return 1;
    }
}
