package de.pcps.jamtugether.model.soundtrack.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.crypto.spec.PSource;

import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import timber.log.Timber;

/*
 * represents a soundtrack from a UI standpoint
 * this can either be a single or composite soundtrack
 */
public abstract class Soundtrack {

    @NonNull
    private final MutableLiveData<State> state;

    @NonNull
    private final MutableLiveData<Integer> progress;

    private float volume;

    public Soundtrack() {
        this.state = new MutableLiveData<>(State.IDLE);
        this.progress = new MutableLiveData<>(0);
        this.volume = 100f;
        if(this instanceof CompositeSoundtrack) {
            Timber.d("composite soundtrack created: %s, %d", hashCode(), this.progress.getValue());
        }
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

    public void setState(@NonNull State state) {
        this.state.setValue(state);
    }

    public void postState(@NonNull State state) {
        this.state.postValue(state);
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    @NonNull
    public LiveData<Integer> getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress.setValue(progress);
    }

    public void postProgress(int progress) {
        if(this instanceof CompositeSoundtrack) {
            Timber.d("postProgress(): %d, hashCode(): %s", progress, this.hashCode());
        }
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
