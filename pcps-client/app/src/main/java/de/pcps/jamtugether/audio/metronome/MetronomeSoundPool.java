package de.pcps.jamtugether.audio.metronome;

import android.content.Context;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.audio.sound.pool.base.BaseSoundPool;

public class MetronomeSoundPool extends BaseSoundPool {

    public MetronomeSoundPool(@NonNull Context context) {
        super(context, 1, Metronome.SOUND);
    }

    @Override
    public int play(int soundID, float pitch) {
        return soundPool.play(Metronome.SOUND,1.0f,1.0f,0,0,1);
    }
}
