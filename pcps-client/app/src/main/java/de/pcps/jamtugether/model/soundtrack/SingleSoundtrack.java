package de.pcps.jamtugether.model.soundtrack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;

import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;

public class SingleSoundtrack extends Soundtrack implements Cloneable {

    private final int userID;

    @NonNull
    private final List<Sound> soundSequence;

    public SingleSoundtrack(int userID, @NonNull List<Sound> soundSequence) {
        super();
        this.userID = userID;
        this.soundSequence = soundSequence;
    }

    @NonNull
    @Override
    public SingleSoundtrack clone() throws CloneNotSupportedException {
        return (SingleSoundtrack) super.clone();
    }

    public int getUserID() {
        return userID;
    }

    @NonNull
    public List<Sound> getSoundSequence() {
        return soundSequence;
    }

    @NonNull
    public List<Sound> getSoundsFor(int currentTime) {
        List<Sound> sounds = new ArrayList<>();
        for (Sound sound : soundSequence) {
            if (justResumed) {
                // add sounds that were interrupted because of pause + sounds that start at given time
                if (sound.getStartTime() <= currentTime && currentTime < sound.getEndTime()) {
                    sounds.add(sound);
                }
            } else {
                // only add sounds that start at given time
                if (sound.getStartTime() == currentTime) {
                    sounds.add(sound);
                }
            }
        }
        return sounds;
    }

    @Nullable
    public String getInstrument() {
        if (soundSequence.isEmpty()) {
            return null;
        }
        return soundSequence.get(0).getInstrument();
    }

    @Override
    public int getLength() {
        if (soundSequence.isEmpty()) {
            return 0;
        }
        Sound firstSound = soundSequence.get(0);
        Sound lastSound = soundSequence.get(soundSequence.size() - 1);
        return lastSound.getEndTime() - firstSound.getStartTime();
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
        void onDeleteSoundtrackButtonClicked(@NonNull SingleSoundtrack singleSoundtrack);
    }
}
