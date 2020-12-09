package de.pcps.jamtugether.models;

import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.pcps.jamtugether.R;

public class Soundtrack extends ArrayList<Sound> {

    // todo check this attribute with server group
    private final int id;

    // todo ignore other attributes in json

    @NonNull
    private final MutableLiveData<Float> volume;

    @NonNull
    private final MutableLiveData<Integer> playPauseButtonImageResource;

    @NonNull
    private final MutableLiveData<Integer> stopButtonVisibility;

    public Soundtrack(int id) {
        this.id = id;
        this.volume = new MutableLiveData<>(0f);
        this.playPauseButtonImageResource = new MutableLiveData<>(R.drawable.ic_play);
        this.stopButtonVisibility = new MutableLiveData<>(View.INVISIBLE);
    }

    public int getID() {
        return id;
    }

    public void setVolume(float volume) {
        this.volume.setValue(volume);
    }

    @NonNull
    public LiveData<Float> getVolume() {
        return volume;
    }

    public void setPlayPauseButtonImageResource(@DrawableRes int playPauseButtonImageResource) {
        this.playPauseButtonImageResource.setValue(playPauseButtonImageResource);
    }

    @NonNull
    public MutableLiveData<Integer> getPlayPauseButtonImageResource() {
        return playPauseButtonImageResource;
    }

    public void setStopButtonVisibility(int stopButtonVisibility) {
        this.stopButtonVisibility.setValue(stopButtonVisibility);
    }

    @NonNull
    public MutableLiveData<Integer> getStopButtonVisibility() {
        return stopButtonVisibility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Soundtrack soundtrack = (Soundtrack) o;
        return id == soundtrack.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    public static Soundtrack compositeFrom(@NonNull List<Soundtrack> soundtracks) {
        // todo
        return new Soundtrack(0);
    }

    public static DiffUtil.ItemCallback<Soundtrack> DIFF_UTIL_CALLBACK = new DiffUtil.ItemCallback<Soundtrack>() {
        @Override
        public boolean areItemsTheSame(@NonNull Soundtrack oldItem, @NonNull Soundtrack newItem) {
            return oldItem.getID() == newItem.getID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Soundtrack oldItem, @NonNull Soundtrack newItem) {
            return oldItem.equals(newItem);
        }
    };

    public interface OnChangeListener {

        void onVolumeChanged(@NonNull Soundtrack soundtrack, float volume);

        void onPlayPauseButtonClicked(@NonNull Soundtrack soundtrack);

        void onStopButtonClicked(@NonNull Soundtrack soundtrack);

        void onFastForwardButtonClicked(@NonNull Soundtrack soundtrack);

        void onFastRewindButtonClicked(@NonNull Soundtrack soundtrack);
    }

    public interface OnDeleteListener {
        void onDeleteButtonClicked(@NonNull Soundtrack soundtrack);
    }
}
