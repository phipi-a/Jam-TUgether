package de.pcps.jamtugether.models.music.soundtrack;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.pcps.jamtugether.models.music.sound.Sound;

public class SingleSoundtrack extends Soundtrack {

    private final int userID;

    @NonNull
    private final List<Sound> soundSequence;

    public SingleSoundtrack(int userID, @NonNull List<Sound> soundSequence) {
        super();
        this.userID = userID;
        this.soundSequence = soundSequence;
    }

    // just for testing
    // todo remove later
    public SingleSoundtrack(int userID) {
        this(userID, new ArrayList<>());
    }

    public int getID() {
        return userID;
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
        return userID == singleSoundtrack.userID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userID);
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
