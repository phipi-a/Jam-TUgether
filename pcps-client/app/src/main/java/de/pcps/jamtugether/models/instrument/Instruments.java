package de.pcps.jamtugether.models.instrument;

import androidx.annotation.NonNull;

import java.util.HashMap;

public class Instruments {

    @NonNull
    public static final Flute FLUTE = Flute.getInstance();

    @NonNull
    public static final Drums DRUMS = Drums.getInstance();

    @NonNull
    public static final Shaker SHAKER = Shaker.getInstance();

    @NonNull
    public static final Instrument FALLBACK = FLUTE;

    @NonNull
    public static final Instrument[] LIST = {FLUTE, DRUMS, SHAKER};

    @NonNull
    private static final HashMap<String, Instrument> preferenceMap = new HashMap<>();

    static {
        for(Instrument instrument : LIST) {
            preferenceMap.put(instrument.getPreferenceValue(), instrument);
        }
    }

    @NonNull
    public static Instrument from(@NonNull String preferenceValue) {
        Instrument instrument= preferenceMap.get(preferenceValue);
        return instrument != null ? instrument : FALLBACK;
    }
}
