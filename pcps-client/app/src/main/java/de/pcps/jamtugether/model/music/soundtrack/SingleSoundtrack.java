package de.pcps.jamtugether.model.music.soundtrack;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;

import de.pcps.jamtugether.model.music.sound.Sound;
import de.pcps.jamtugether.model.music.soundtrack.base.Soundtrack;

public class SingleSoundtrack extends Soundtrack {

    private final int userID;

    @NonNull
    private final List<Sound> soundSequence;

    public SingleSoundtrack(int userID, @NonNull List<Sound> soundSequence) {
        super();
        this.userID = userID;
        this.soundSequence = soundSequence;
    }

    public SingleSoundtrack(int userID) {
        this(userID, new ArrayList<>());
    }

    public int getUserID() {
        return userID;
    }

    @NonNull
    public List<Sound> getSoundSequence() {
        return soundSequence;
    }

    public static DiffUtil.ItemCallback<SingleSoundtrack> DIFF_UTIL_CALLBACK = new DiffUtil.ItemCallback<SingleSoundtrack>() {
        @Override
        public boolean areItemsTheSame(@NonNull SingleSoundtrack oldItem, @NonNull SingleSoundtrack newItem) {
            return oldItem.getUserID() == newItem.getUserID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull SingleSoundtrack oldItem, @NonNull SingleSoundtrack newItem) {
            return oldItem.soundSequence.equals(newItem.soundSequence);
        }
    };

    public interface OnDeleteListener {
        void onDeleteButtonClicked(@NonNull SingleSoundtrack singleSoundtrack);
    }
}
