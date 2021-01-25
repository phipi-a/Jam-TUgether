package de.pcps.jamtugether.audio.metronome;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.beat.Beat;

public class Metronome {

    @RawRes
    public static final int SOUND = R.raw.metronome;

    @NonNull
    private Beat beat = Beat.DEFAULT;

    @Nullable
    private MetronomeSoundPool soundPool;

    @Nullable
    private static Metronome instance;

    @NonNull
    private final MutableLiveData<Boolean> playing;

    public Metronome() {
        playing = new MutableLiveData<>(false);
    }

    public void loadSounds(@NonNull Context context) {
        soundPool = new MetronomeSoundPool(context);
    }

    public void play(int sound) {
        if (soundPool != null && playing.getValue() != null && !playing.getValue()) {
            soundPool.stopAllSounds();
            soundPool.playSoundRes(sound, -1);
        }
    }

    public void stop() {
        if (soundPool != null) {
            soundPool.stopAllSounds();
        }
    }

    public void updateBeat(@NonNull Beat beat) {
        this.beat = beat;
    }

    public void postPlaying(boolean playing) {
        this.playing.postValue(playing);
    }

    @NonNull
    public Beat getBeat() {
        return beat;
    }

    @NonNull
    public LiveData<Boolean> getPlaying() {
        return playing;
    }

    @NonNull
    public static Metronome getInstance() {
        if (instance == null) {
            instance = new Metronome();
        }
        return instance;
    }

    public interface OnChangeCallback {

        void onBeatChanged(@NonNull Beat beat);

        void onPlayStopButtonClicked();
    }
}
