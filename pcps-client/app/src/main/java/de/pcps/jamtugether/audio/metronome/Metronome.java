package de.pcps.jamtugether.audio.metronome;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.pcps.jamtugether.model.beat.Beat;
import timber.log.Timber;

public class Metronome {

    @NonNull
    private Beat beat = Beat.DEFAULT;

    @Nullable
    private MetronomeSoundPool soundPool;

    @Nullable
    private static Metronome instance;

    public void loadSounds(@NonNull Context context) {
        soundPool = new MetronomeSoundPool(context);
    }

    public void playSound(int sound) {
        if (soundPool != null) {
            soundPool.playSoundRes(sound);
        }
    }

    public void stop() {
        if (soundPool != null) {
            soundPool.stopAllSounds();
        }
    }

    public void updateBeat(@NonNull Beat beat) {
        Timber.d("beat: %d, %d", beat.getTempo(), beat.getTicksPerTact());
        this.beat = beat;
    }

    @NonNull
    public Beat getBeat() {
        return beat;
    }

    @NonNull
    public static Metronome getInstance() {
        if (instance == null) {
            instance = new Metronome();
        }
        return instance;
    }
}
