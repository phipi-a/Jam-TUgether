package de.pcps.jamtugether.model.soundtrack;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.base.Instruments;
import de.pcps.jamtugether.audio.sound.pool.base.BaseSoundPool;
import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;

public class SingleSoundtrack extends Soundtrack {

    @NonNull
    public static final DiffUtil.ItemCallback<SingleSoundtrack> DIFF_UTIL_CALLBACK = new DiffUtil.ItemCallback<SingleSoundtrack>() {
        @Override
        public boolean areItemsTheSame(@NonNull SingleSoundtrack oldItem, @NonNull SingleSoundtrack newItem) {
            return oldItem.getID().equals(newItem.getID());
        }

        @Override
        public boolean areContentsTheSame(@NonNull SingleSoundtrack oldItem, @NonNull SingleSoundtrack newItem) {
            return oldItem.soundSequence.equals(newItem.soundSequence);
        }
    };

    private final int userID;

    @NonNull
    private final String instrument;

    @NonNull
    private final List<Sound> soundSequence;

    private final boolean isOwnSoundtrack;

    @Nullable
    private BaseSoundPool soundPool;

    public SingleSoundtrack(int userID, @NonNull String instrument, @NonNull List<Sound> soundSequence, boolean isOwnSoundtrack) {
        super();
        this.userID = userID;
        this.instrument = instrument;
        this.soundSequence = soundSequence;
        this.isOwnSoundtrack = isOwnSoundtrack;
    }

    // just used for empty soundtrack, this is never sent to server
    public SingleSoundtrack(int userID) {
        this(userID, Instruments.FALLBACK.getServerString(), new ArrayList<>(), false);
    }

    // own soundtrack object
    public SingleSoundtrack(int userID, @NonNull String instrument) {
        this(userID, instrument, new ArrayList<>(), true);
    }

    public SingleSoundtrack(int userID, @NonNull String instrument, @NonNull List<Sound> soundSequence) {
        this(userID, instrument, soundSequence, false);
    }

    public void loadSounds(@NonNull Context context) {
        soundPool = getInstrument().createSoundPool(context);
    }

    public void addSound(Sound sound) {
        soundSequence.add(sound);
    }

    @Override
    public int getLength() {
        if (isEmpty()) {
            return 0;
        }
        Sound firstSound = soundSequence.get(0);
        Sound lastSound = soundSequence.get(soundSequence.size() - 1);
        return lastSound.getEndTime() - firstSound.getStartTime();
    }

    @Override
    public boolean isEmpty() {
        return soundSequence.isEmpty();
    }

    public boolean isOwnSoundtrack() {
        return isOwnSoundtrack;
    }

    @NonNull
    public List<Sound> getSoundsFor(int currentTime, boolean finishSounds) {
        List<Sound> sounds = new ArrayList<>();
        for (Sound sound : soundSequence) {
            // add sounds that start at given time
            if (sound.getStartTime() == currentTime) {
                sounds.add(sound);
            }
            boolean soundNeedsToBeResumed = getInstrument().soundsNeedToBeResumed();

            if (finishSounds && soundNeedsToBeResumed) {
                // finish sounds that were interrupted because of pause or that were jumped to because of forwarding
                if (sound.getStartTime() < currentTime && currentTime < sound.getEndTime()) {
                    sounds.add(sound);
                }
            }
        }
        return sounds;
    }

    @NonNull
    public String getID() {
        return String.valueOf(userID).concat(instrument);
    }

    public int getUserID() {
        return userID;
    }

    @NonNull
    public Instrument getInstrument() {
        return Instruments.fromServer(instrument);
    }

    @NonNull
    public List<Sound> getSoundSequence() {
        return soundSequence;
    }

    @Nullable
    public BaseSoundPool getSoundPool() {
        return soundPool;
    }

    @NonNull
    public SingleSoundtrack clone(@NonNull Context context) {
        SingleSoundtrack cloned = new SingleSoundtrack(-1, this.instrument, this.soundSequence);
        cloned.loadSounds(context);
        return cloned;
    }

    public interface OnDeleteListener {
        void onDeleteSoundtrackButtonClicked(@NonNull SingleSoundtrack singleSoundtrack);
    }
}
