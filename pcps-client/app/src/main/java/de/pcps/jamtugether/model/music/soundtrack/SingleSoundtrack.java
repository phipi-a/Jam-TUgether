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

    public int getLength() {
        if(soundSequence.isEmpty()) {
            return 0;
        }
        Sound firstSound = soundSequence.get(0);
        Sound lastSound = soundSequence.get(soundSequence.size() - 1);
        return lastSound.getEndTime() - firstSound.getStartTime();
    }

    public interface OnDeleteListener {
        void onDeleteSoundtrackButtonClicked(@NonNull SingleSoundtrack singleSoundtrack);
    }
}
