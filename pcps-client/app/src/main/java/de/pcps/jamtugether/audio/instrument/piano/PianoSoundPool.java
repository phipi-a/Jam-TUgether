package de.pcps.jamtugether.audio.instrument.piano;

import android.content.Context;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.audio.sound.BaseSoundPool;

public class PianoSoundPool extends BaseSoundPool {

    private static final int SOUND_POOL_MAX_STREAMS = 100;

    public PianoSoundPool(@NonNull Context context) {
        super(context, SOUND_POOL_MAX_STREAMS, PianoSound.values(), 0);
    }
}
