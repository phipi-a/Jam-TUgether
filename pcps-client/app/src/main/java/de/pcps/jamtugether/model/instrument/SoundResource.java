package de.pcps.jamtugether.model.instrument;

import java.util.HashMap;

import de.pcps.jamtugether.R;

public enum SoundResource {

    FLUTE(R.raw.flute_sound, 0),

    SNARE(R.raw.drum_snare, 1),
    KICK(R.raw.drum_kick, 2),
    HAT(R.raw.drum_hat, 3),
    CYMBAL(R.raw.drum_cymbal, 4);

    private final int soundResID;

    private final int element;

    SoundResource(int soundResID, int element) {
        this.soundResID = soundResID;
        this.element = element;
    }

    private static final HashMap<Integer, SoundResource> pitchMap = new HashMap<>();

    static {
        for(SoundResource soundResource : values()) {
            pitchMap.put(soundResource.element, soundResource);
        }
    }

    public int getSoundResID() {
        return soundResID;
    }

    public static SoundResource from(int element) {
        return pitchMap.get(element);
    }
}
