package de.pcps.jamtugether.audio.sound.pool;

import android.content.Context;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.audio.sound.pool.base.BaseSoundPool;
import de.pcps.jamtugether.model.sound.shaker.ShakerSound;

public class ShakerSoundPool extends BaseSoundPool {

    private static final int SOUND_POOL_MAX_STREAMS = 100;

    public ShakerSoundPool(@NonNull Context context) {
        super(context, SOUND_POOL_MAX_STREAMS, ShakerSound.values());
    }
}
