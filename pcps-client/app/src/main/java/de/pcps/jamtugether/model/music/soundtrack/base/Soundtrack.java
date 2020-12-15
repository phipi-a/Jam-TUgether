package de.pcps.jamtugether.model.music.soundtrack.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/*
 * represents a soundtrack from a UI standpoint
 * this can either be a single or composite soundtrack
 */
public abstract class Soundtrack {

    @NonNull
    private final MutableLiveData<State> state;

    @NonNull
    private final MutableLiveData<Integer> progress; // 1 - 100

    @NonNull
    private final MutableLiveData<Float> volume;

    public Soundtrack() {
        this.state = new MutableLiveData<>(State.IDLE);
        this.volume = new MutableLiveData<>(0f);
        this.progress = new MutableLiveData<>(0);
    }

    @NonNull
    public LiveData<State> getState() {
        return state;
    }

    @NonNull
    public LiveData<Integer> getProgress() {
        return progress;
    }

    @NonNull
    public LiveData<Float> getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume.setValue(volume);
    }

    public void play() {
        state.setValue(State.PLAYING);
        // todo update progress regularly
    }

    public void fastForward() {
        // todo
        //  update progress
    }

    public void fastRewind() {
        // todo
        //  update progress
    }

    public void pause() {
        state.setValue(State.PAUSED);
        // todo
    }

    public void resume() {
        // todo
    }

    public void stop() {
        state.setValue(State.STOPPED);
        // todo reset progress
    }

    public enum State {
        IDLE,
        PLAYING,
        PAUSED,
        STOPPED
    }

    public interface OnChangeListener {

        void onVolumeChanged(@NonNull Soundtrack soundtrack, float volume);

        void onPlayPauseButtonClicked(@NonNull Soundtrack soundtrack);

        void onStopButtonClicked(@NonNull Soundtrack soundtrack);

        void onFastForwardButtonClicked(@NonNull Soundtrack soundtrack);

        void onFastRewindButtonClicked(@NonNull Soundtrack soundtrack);
    }
}
