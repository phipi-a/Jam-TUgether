package de.pcps.jamtugether.models.instruments;

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
    public static final Instrument FALLBACK = FLUTE; // todo maybe replace fallback with error message

    @NonNull
    public static final Instrument[] LIST = {FLUTE, DRUMS, SHAKER};

    @NonNull
    private static final HashMap<String, Instrument> preferenceMap = new HashMap<>();

    @NonNull
    private static final HashMap<String, Instrument> serverMap = new HashMap<>();

    static {
        for(Instrument instrument : LIST) {
            preferenceMap.put(instrument.getPreferenceValue(), instrument);
            serverMap.put(instrument.getServerString(), instrument);
        }
    }

    @NonNull
    public static Instrument fromPreferences(@NonNull String preferenceValue) {
        Instrument instrument = preferenceMap.get(preferenceValue);
        return instrument != null ? instrument : FALLBACK;
    }

    @NonNull
    public static Instrument fromServer(@NonNull String serverString) {
        Instrument instrument = serverMap.get(serverString);
        return instrument != null ? instrument : FALLBACK;
    }
}
