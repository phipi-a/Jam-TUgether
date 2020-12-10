package de.pcps.jamtugether.models.music.soundtrack;

import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import de.pcps.jamtugether.R;

/*
 * represents a soundtrack from a UI standpoint
 * this can either be a single or composite soundtrack
 */
public abstract class Soundtrack {

    @NonNull
    private final MutableLiveData<Float> volume;

    @NonNull
    private final MutableLiveData<Integer> playPauseButtonImageResource;

    @NonNull
    private final MutableLiveData<Integer> stopButtonVisibility;

    public Soundtrack() {
        this.volume = new MutableLiveData<>(0f);
        this.playPauseButtonImageResource = new MutableLiveData<>(R.drawable.ic_play);
        this.stopButtonVisibility = new MutableLiveData<>(View.INVISIBLE);
    }

    @NonNull
    public LiveData<Float> getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume.setValue(volume);
    }

    @NonNull
    public LiveData<Integer> getPlayPauseButtonImageResource() {
        return playPauseButtonImageResource;
    }

    public void setPlayPauseButtonImageResource(@DrawableRes int playPauseButtonImageResource) {
        this.playPauseButtonImageResource.setValue(playPauseButtonImageResource);
    }

    @NonNull
    public LiveData<Integer> getStopButtonVisibility() {
        return stopButtonVisibility;
    }

    public void setStopButtonVisibility(int stopButtonVisibility) {
        this.stopButtonVisibility.setValue(stopButtonVisibility);
    }

    public interface OnChangeListener {

        void onVolumeChanged(@NonNull Soundtrack soundtrack, float volume);

        void onPlayPauseButtonClicked(@NonNull Soundtrack soundtrack);

        void onStopButtonClicked(@NonNull Soundtrack soundtrack);

        void onFastForwardButtonClicked(@NonNull Soundtrack soundtrack);

        void onFastRewindButtonClicked(@NonNull Soundtrack soundtrack);
    }
}
