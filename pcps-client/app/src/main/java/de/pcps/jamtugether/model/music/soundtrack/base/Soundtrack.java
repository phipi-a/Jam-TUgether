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

    public void togglePlayPause() {
        if(state.getValue() == null) {
            return;
        }

        switch (state.getValue()) {
            case PLAYING:
                pause();
                break;
            case PAUSED:
                resume();
                break;
            case IDLE:
            case STOPPED:
                play();
        }
    }

    private void play() {
        state.setValue(State.PLAYING);
        // todo update progress regularly
        //  continue at progress
    }

    private void pause() {
        state.setValue(State.PAUSED);
        // todo
    }

    private void resume() {
        play();
    }

    public void fastForward() {
        // todo
        //  update progress
    }

    public void fastRewind() {
        // todo
        //  update progress
    }

    public void stop() {
        state.setValue(State.STOPPED);
        progress.setValue(0);
        // todo reset progress
    }

    public enum State {
        IDLE,
        PLAYING,
        PAUSED,
        STOPPED
    }
}