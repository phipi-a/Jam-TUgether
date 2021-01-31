package de.pcps.jamtugether.audio.instrument.drums;

import android.content.Context;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.audio.sound.BaseSoundPool;

public class DrumsSoundPool extends BaseSoundPool {

    /*
     this is necessary in order to play a lot of
     sounds in a short period of time because these sounds
     start before another one ends
     */;
    private static final int SOUND_POOL_MAX_STREAMS = 100;

    public DrumsSoundPool(@NonNull Context context) {
        super(context, SOUND_POOL_MAX_STREAMS, DrumsSound.values(), 0);
    }
}
