package de.pcps.jamtugether.audio.instrument.flute;

import android.content.Context;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.audio.sound.BaseSoundPool;

public class FluteSoundPool extends BaseSoundPool {

    public FluteSoundPool(@NonNull Context context) {
        super(context, 1, FluteSound.values(), 0);
        // todo change loop to -1
    }
}
