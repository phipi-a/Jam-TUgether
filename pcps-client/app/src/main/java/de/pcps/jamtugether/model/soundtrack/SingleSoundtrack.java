package de.pcps.jamtugether.model.soundtrack;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.base.Instruments;
import de.pcps.jamtugether.audio.sound.BaseSoundPool;
import de.pcps.jamtugether.model.Sound;
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

    private int userID;

    private String userName;

    private Instrument instrument;

    private int number;

    private List<Sound> soundSequence;

    private transient boolean representsOwnSoundtrack;

    @Nullable
    private transient BaseSoundPool soundPool;

    // only for Moshi
    private SingleSoundtrack() {
    }

    // soundtrack from server
    public SingleSoundtrack(int userID, @NonNull String userName, @NonNull Instrument instrument, int number, @NonNull List<Sound> soundSequence) {
        this(userID, userName, instrument, number, soundSequence, false);
    }

    // empty soundtrack (never sent to server)
    public SingleSoundtrack(String ignore) { // ignore only needed so there's no overlap with private constructor that Moshi uses
        this(-1, "", Instruments.DEFAULT, -1, new ArrayList<>(), true);
    }

    // own soundtrack object (starts with empty array list)
    public SingleSoundtrack(int userID, @NonNull String userName, @NonNull Instrument instrument, int number) {
        this(userID, userName, instrument, number, new ArrayList<>(), true);
    }

    private SingleSoundtrack(int userID, @NonNull String userName, @NonNull Instrument instrument, int number, @NonNull List<Sound> soundSequence, boolean representsOwnSoundtrack) {
        super();
        this.userID = userID;
        this.userName = userName;
        this.instrument = instrument;
        this.number = number;
        this.soundSequence = soundSequence;
        this.representsOwnSoundtrack = representsOwnSoundtrack;
    }

    public void loadSounds(@NonNull Context context) {
        soundPool = getInstrument().createSoundPool(context);
    }

    public void addSound(Sound sound) {
        soundSequence.add(sound);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SingleSoundtrack that = (SingleSoundtrack) o;
        return getID().equals(that.getID()) && soundSequence.equals(that.soundSequence);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, userName, instrument, number, soundSequence, representsOwnSoundtrack, soundPool);
    }

    @NonNull
    @Override
    public String toString() {
        return "SingleSoundtrack{" +
                "userID=" + userID +
                ", userName='" + userName + '\'' +
                ", instrument=" + instrument +
                ", number=" + number +
                ", volume=" + getVolume() +
                '}';
    }

    @Override
    public int getLength() {
        if (isEmpty()) {
            return 0;
        }
        return soundSequence.get(soundSequence.size() - 1).getEndTime();
    }

    @Override
    public boolean isEmpty() {
        return soundSequence.isEmpty();
    }

    @Nullable
    @Override
    public String getLabel(@NonNull Context context) {
        if (representsOwnSoundtrack) {
            return context.getString(R.string.own_soundtrack);
        }
        return userName;
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
        return String.valueOf(userID).concat(instrument.getServerString()).concat(String.valueOf(number));
    }

    public int getUserID() {
        return userID;
    }

    @NonNull
    public String getUserName() {
        return userName;
    }

    @NonNull
    public Instrument getInstrument() {
        return instrument;
    }

    public int getNumber() {
        return number;
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
        SingleSoundtrack cloned = new SingleSoundtrack(-1, this.userName, this.instrument, -1, this.soundSequence);
        cloned.loadSounds(context);
        return cloned;
    }

    public interface OnDeleteListener {
        void onDeleteSoundtrackButtonClicked(@NonNull SingleSoundtrack singleSoundtrack);
    }
}
