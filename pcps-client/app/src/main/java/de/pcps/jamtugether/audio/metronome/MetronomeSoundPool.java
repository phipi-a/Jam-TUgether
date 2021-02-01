package de.pcps.jamtugether.audio.metronome;

import android.content.Context;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.audio.sound.BaseSoundPool;

public class MetronomeSoundPool extends BaseSoundPool {

    public MetronomeSoundPool(@NonNull Context context) {
        super(context, 1, MetronomeSound.values(), 0);
    }
}
