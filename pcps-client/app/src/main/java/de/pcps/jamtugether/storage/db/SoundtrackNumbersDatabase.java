package de.pcps.jamtugether.storage.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.drums.Drums;
import de.pcps.jamtugether.audio.instrument.flute.Flute;
import de.pcps.jamtugether.audio.instrument.shaker.Shaker;
import de.pcps.jamtugether.model.User;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.utils.SoundtrackUtils;

@Singleton
public class SoundtrackNumbersDatabase {

    @NonNull
    private final List<Integer> usedNumbersForFlute = new ArrayList<>();

    @NonNull
    private final List<Integer> usedNumbersForDrums = new ArrayList<>();

    @NonNull
    private final List<Integer> usedNumbersForShaker = new ArrayList<>();

    @NonNull
    private final List<Integer> usedNumbersForPiano = new ArrayList<>();

    @Nullable
    private List<SingleSoundtrack> previousSoundtracks;

    @Inject
    public SoundtrackNumbersDatabase(@NonNull RoomRepository roomRepository, @NonNull SoundtrackRepository soundtrackRepository) {
        Observer<List<SingleSoundtrack>> soundtracksObserver = soundtracks -> {
            User user = roomRepository.getUser();

            if (user != null && previousSoundtracks != null) {
                for (SingleSoundtrack soundtrack : SoundtrackUtils.getOwnDeletedSoundtracks(user, previousSoundtracks, soundtracks)) {
                    onSoundtrackDeleted(soundtrack);
                }
            }
            previousSoundtracks = soundtracks;
        };

        roomRepository.getUserInRoom().observeForever(userInRoom -> {
            if (userInRoom) {
                soundtrackRepository.getAllSoundtracks().observeForever(soundtracksObserver);
            } else {
                onUserLeftRoom();
                soundtrackRepository.getAllSoundtracks().removeObserver(soundtracksObserver);
            }
        });
    }

    private void onUserLeftRoom() {
        usedNumbersForFlute.clear();
        usedNumbersForDrums.clear();
        usedNumbersForShaker.clear();
        usedNumbersForPiano.clear();
    }

    @NonNull
    private List<Integer> getListFrom(@NonNull Instrument instrument) {
        if (instrument == Flute.getInstance()) {
            return usedNumbersForFlute;
        } else if (instrument == Drums.getInstance()) {
            return usedNumbersForDrums;
        } else if (instrument == Shaker.getInstance()) {
            return usedNumbersForShaker;
        }
        return usedNumbersForPiano;
    }

    public void onSoundtrackCreated(@NonNull SingleSoundtrack ownSoundtrack) {
        getListFrom(ownSoundtrack.getInstrument()).add(ownSoundtrack.getNumber());
    }

    public void onSoundtrackDeleted(@NonNull SingleSoundtrack soundtrack) {
        getListFrom(soundtrack.getInstrument()).remove(Integer.valueOf(soundtrack.getNumber()));
    }

    public int getUnusedNumberFor(@NonNull Instrument instrument) {
        List<Integer> usedNumbers = getListFrom(instrument);
        int number = 1;
        Collections.sort(usedNumbers);

        for (int current : usedNumbers) {
            if (number < current) {
                return number;
            } else {
                number++;
            }
        }
        return number;
    }
}
