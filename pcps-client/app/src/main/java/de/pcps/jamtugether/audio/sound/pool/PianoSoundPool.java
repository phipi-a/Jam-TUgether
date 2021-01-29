package de.pcps.jamtugether.audio.sound.pool;

import android.content.Context;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.audio.sound.pool.base.BaseSoundPool;
import de.pcps.jamtugether.model.sound.piano.PianoSound;

public class PianoSoundPool extends BaseSoundPool {

    private static final int SOUND_POOL_MAX_STREAMS = 100;

    public PianoSoundPool(@NonNull Context context) {
        super(context, SOUND_POOL_MAX_STREAMS, PianoSound.values());
        this.loop = -1;
    }
}
