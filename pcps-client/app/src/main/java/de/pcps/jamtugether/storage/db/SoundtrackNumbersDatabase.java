package de.pcps.jamtugether.storage.db;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.drums.Drums;
import de.pcps.jamtugether.audio.instrument.flute.Flute;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;

@Singleton
public class SoundtrackNumbersDatabase {

    @NonNull
    private final List<Integer> usedNumbersForFlute = new ArrayList<>();

    @NonNull
    private final List<Integer> usedNumbersForDrums = new ArrayList<>();

    @NonNull
    private final List<Integer> usedNumbersForShaker = new ArrayList<>();

    @Inject
    public SoundtrackNumbersDatabase(@NonNull RoomRepository roomRepository) {
        roomRepository.getUserInRoom().observeForever(userInRoom -> {
            if(!userInRoom) {
                onUserLeftRoom();
            }
        });
    }

    private void onUserLeftRoom() {
        usedNumbersForFlute.clear();
        usedNumbersForDrums.clear();
        usedNumbersForShaker.clear();
    }

    @NonNull
    private List<Integer> getListFrom(@NonNull Instrument instrument) {
        if (instrument == Flute.getInstance()) {
            return usedNumbersForFlute;
        } else if (instrument == Drums.getInstance()) {
            return usedNumbersForDrums;
        }
        return usedNumbersForShaker;
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
