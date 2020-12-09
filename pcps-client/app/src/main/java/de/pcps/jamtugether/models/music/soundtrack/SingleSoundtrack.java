package de.pcps.jamtugether.models.music.soundtrack;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.pcps.jamtugether.models.music.sound.Sound;

public class SingleSoundtrack extends Soundtrack {

    private final int id;

    @NonNull
    private final List<Sound> soundSequence;

    public SingleSoundtrack(int id, @NonNull List<Sound> soundSequence) {
        super();
        this.id = id;
        this.soundSequence = soundSequence;
    }

    // just for testing
    // todo remove later
    public SingleSoundtrack(int id) {
        this(id, new ArrayList<>());
    }

    public int getID() {
        return id;
    }

    @NonNull
    public List<Sound> getSoundSequence() {
        return soundSequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SingleSoundtrack singleSoundtrack = (SingleSoundtrack) o;
        return id == singleSoundtrack.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    public static DiffUtil.ItemCallback<SingleSoundtrack> DIFF_UTIL_CALLBACK = new DiffUtil.ItemCallback<SingleSoundtrack>() {
        @Override
        public boolean areItemsTheSame(@NonNull SingleSoundtrack oldItem, @NonNull SingleSoundtrack newItem) {
            return oldItem.getID() == newItem.getID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull SingleSoundtrack oldItem, @NonNull SingleSoundtrack newItem) {
            return oldItem.equals(newItem);
        }
    };

    public interface OnDeleteListener {
        void onDeleteButtonClicked(@NonNull SingleSoundtrack singleSoundtrack);
    }
}
