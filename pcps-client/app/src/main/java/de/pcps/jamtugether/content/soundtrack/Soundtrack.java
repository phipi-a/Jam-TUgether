package de.pcps.jamtugether.content.soundtrack;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.pcps.jamtugether.content.instrument.Instrument;

public class Soundtrack extends ArrayList<Sound> {

    // todo check with server group
    private final int id;

    /*
     * this attribute is necessary in order to set the volume slider accordingly
     * Otherwise, the slider resets on configuration changes
     */
    private float volume;

    public Soundtrack(int id) {
        this.id = id;
        this.volume = 0;
    }

    public int getID() {
        return id;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
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

        void onDeleteButtonClicked(@NonNull Soundtrack soundtrack);
    }
}
