package de.pcps.jamtugether.model.instrument.base;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.model.instrument.Drums;
import de.pcps.jamtugether.model.instrument.Flute;
import de.pcps.jamtugether.model.instrument.Shaker;

@Singleton
public class Instruments {

    @NonNull
    private final Flute flute;

    @NonNull
    private final Drums drums;

    @NonNull
    private final Shaker shaker;

    @NonNull
    private final List<Instrument> list;

    @NonNull
    private final Instrument fallback; // todo maybe replace fallback with error message

    @NonNull
    private final HashMap<String, Instrument> preferenceMap = new HashMap<>();

    @NonNull
    private final HashMap<String, Instrument> serverMap = new HashMap<>();

    @Inject
    public Instruments(@NonNull Flute flute, @NonNull Drums drums, @NonNull Shaker shaker) {
        this.flute = flute;
        this.drums = drums;
        this.shaker = shaker;

        this.list = new ArrayList<>();
        this.list.add(flute);
        this.list.add(drums);
        this.list.add(shaker);

        this.fallback = flute;

        for(Instrument instrument : list) {
            preferenceMap.put(instrument.getPreferenceValue(), instrument);
            serverMap.put(instrument.getServerString(), instrument);
        }
    }

    @NonNull
    public Flute getFlute() {
        return flute;
    }

    @NonNull
    public Drums getDrums() {
        return drums;
    }

    @NonNull
    public Shaker getShaker() {
        return shaker;
    }

    @NonNull
    public List<Instrument> getList() {
        return list;
    }

    @NonNull
    public Instrument getFallback() {
        return fallback;
    }

    @NonNull
    public Instrument fromPreferences(@NonNull String preferenceValue) {
        Instrument instrument = preferenceMap.get(preferenceValue);
        return instrument != null ? instrument : fallback;
    }

    @NonNull
    public Instrument fromServer(@NonNull String serverString) {
        Instrument instrument = serverMap.get(serverString);
        return instrument != null ? instrument : fallback;
    }
}
