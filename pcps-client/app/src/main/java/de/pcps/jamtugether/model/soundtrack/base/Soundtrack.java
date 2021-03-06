package de.pcps.jamtugether.model.soundtrack.base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import de.pcps.jamtugether.utils.TimeUtils;

/*
 * represents a soundtrack from a UI standpoint
 * this can either be a single or composite soundtrack
 */
public abstract class Soundtrack {

    public static final long MAX_TIME = TimeUtils.FIVE_MINUTES;

    @NonNull
    private transient final MutableLiveData<State> state;

    @NonNull
    private transient final MutableLiveData<Float> progress;

    private transient int progressInMillis;

    private transient float volume;

    public Soundtrack() {
        this.state = new MutableLiveData<>(State.IDLE);
        this.progress = new MutableLiveData<>(0f);
        this.progressInMillis = 0;
        this.volume = 100f;
    }

    /**
     * @return soundtrack length in millis
     */
    public abstract int getLength();

    public abstract boolean isEmpty();

    @Nullable
    public abstract String getLabel(@NonNull Context context);

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
    public LiveData<Float> getProgress() {
        return progress;
    }

    public int getProgressInMillis() {
        return progressInMillis;
    }

    public void setProgressInMillis(int progressInMillis) {
        this.progressInMillis = progressInMillis;
        this.progress.setValue(calculateProgress(progressInMillis));
    }

    public void postProgressInMillis(int progressInMillis) {
        this.progressInMillis = progressInMillis;
        this.progress.postValue(calculateProgress(progressInMillis));
    }

    private float calculateProgress(int millis) {
        return (millis / (float) this.getLength()) * 100;
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
