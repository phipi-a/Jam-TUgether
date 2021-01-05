package de.pcps.jamtugether.utils.providers;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.drums.Drums;
import de.pcps.jamtugether.audio.instrument.flute.Flute;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;

@Singleton
public class SoundtrackNumberProvider {

    @NonNull
    private final List<Integer> usedNumbersForFlute = new ArrayList<>();

    @NonNull
    private final List<Integer> usedNumbersForDrums = new ArrayList<>();

    @NonNull
    private final List<Integer> usedNumbersForShaker = new ArrayList<>();

    @Inject
    public SoundtrackNumberProvider() { }

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

    public int getFreeNumberFor(@NonNull Instrument instrument) {
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

    public void onUserLeftRoom() {
        usedNumbersForFlute.clear();
        usedNumbersForDrums.clear();
        usedNumbersForShaker.clear();
    }
}
