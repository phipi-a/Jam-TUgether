package de.pcps.jamtugether.model.soundtrack;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;
import de.pcps.jamtugether.model.sound.ServerSound;
import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;

public class SingleSoundtrack extends Soundtrack {

    @NonNull
    public static final DiffUtil.ItemCallback<SingleSoundtrack> DIFF_UTIL_CALLBACK = new DiffUtil.ItemCallback<SingleSoundtrack>() {
        @Override
        public boolean areItemsTheSame(@NonNull SingleSoundtrack oldItem, @NonNull SingleSoundtrack newItem) {
            return oldItem.getUserID() == newItem.getUserID();
        }

        @Override
        public boolean areContentsTheSame(@NonNull SingleSoundtrack oldItem, @NonNull SingleSoundtrack newItem) {
            return oldItem.soundSequence.equals(newItem.soundSequence);
        }
    };

    private final int userID;

    @NonNull
    private final List<Sound> soundSequence;

    private final List<ServerSound> serverSoundSequence;

    private Instrument instrument;

    /**
     * The sound pool on which this soundtrack is being played
     */
    @Nullable
    private BaseSoundPool soundPool;

    public SingleSoundtrack(int userID, @NonNull List<Sound> soundSequence) {
        super();
        this.userID = userID;
        this.soundSequence = soundSequence;
        this.serverSoundSequence = new ArrayList<>();
    }

    public SingleSoundtrack(int userID) {
        this(userID, new ArrayList<>());
    }

    public SingleSoundtrack(int userID, @NonNull Instrument instrument) {
        this(userID);
        this.instrument = instrument;
    }

    public void loadSounds(@NonNull Context context) {
        Instrument instrument;
        if (isEmpty()) {
            instrument = this.instrument;
        } else {
            instrument = soundSequence.get(0).getInstrument();
        }
        soundPool = instrument.createSoundPool(context);
    }

    public void addSound(ServerSound serverSound) {
        serverSoundSequence.add(serverSound);
        soundSequence.add(serverSound);
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

    @Nullable
    public Instrument getInstrument() {
        if (soundSequence.isEmpty()) {
            return instrument;
        }
        return soundSequence.get(0).getInstrument();
    }

    @NonNull
    public List<Sound> getSoundsFor(int currentTime, boolean finishSounds) {
        List<Sound> sounds = new ArrayList<>();
        for (Sound sound : soundSequence) {
            // add sounds that start at given time
            if (sound.getStartTime() == currentTime) {
                sounds.add(sound);
            }
            if (finishSounds) {
                // finish sounds that were interrupted because of pause or that were jumped to because of forwarding
                if (sound.getStartTime() < currentTime && currentTime < sound.getEndTime()) {
                    sounds.add(sound);
                }
            }
        }
        return sounds;
    }

    public int getUserID() {
        return userID;
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
        SingleSoundtrack cloned = new SingleSoundtrack(-1, this.soundSequence);
        cloned.loadSounds(context);
        return cloned;
    }

    public interface OnDeleteListener {
        void onDeleteSoundtrackButtonClicked(@NonNull SingleSoundtrack singleSoundtrack);
    }
}
