package de.pcps.jamtugether.model.soundtrack.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import de.pcps.jamtugether.audio.instrument.base.Instrument;

/*
 * represents a soundtrack from a UI standpoint
 * this can either be a single or composite soundtrack
 */
public abstract class Soundtrack {

    @NonNull
    private final MutableLiveData<State> state;

    @NonNull
    private final MutableLiveData<Float> volume;

    @NonNull
    private final MutableLiveData<Integer> progress;

    public Soundtrack() {
        this.state = new MutableLiveData<>(State.IDLE);
        this.volume = new MutableLiveData<>(100f);
        this.progress = new MutableLiveData<>(0);
    }

    /**
     * @return soundtrack length in millis
     */
    public abstract int getLength();

    public abstract boolean isEmpty();

    @NonNull
    public LiveData<State> getState() {
        return state;
    }

    public void postState(@NonNull State state) {
        this.state.postValue(state);
    }

    @NonNull
    public LiveData<Float> getVolume() {
        return volume;
    }

    public void postVolume(float volume) {
        this.volume.postValue(volume);
    }

    public void setVolume(float volume) {
        this.volume.setValue(volume);
    }

    @NonNull
    public LiveData<Integer> getProgress() {
        return progress;
    }

    public void postProgress(int progress) {
        this.progress.postValue(progress);
    }

    public enum State {
        IDLE,
        PLAYING,
        PAUSED,
        STOPPED
    }

    public interface OnChangeCallback {

        void onVolumeChanged(@NonNull Soundtrack soundtrack, float volume);

        void onPlayPauseButtonClicked(@NonNull Soundtrack soundtrack);

        void onFastForwardButtonClicked(@NonNull Soundtrack soundtrack);

        void onFastRewindButtonClicked(@NonNull Soundtrack soundtrack);

        void onStopButtonClicked(@NonNull Soundtrack soundtrack);
    }
}
