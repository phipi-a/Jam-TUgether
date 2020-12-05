package de.pcps.jamtugether.models;

import androidx.annotation.NonNull;

import java.util.List;

public class VolumeComposition {

    private final int room;

    @NonNull
    private final List<Integer> volumes;

    public VolumeComposition(int room, @NonNull List<Integer> volumes) {
        this.room = room;
        this.volumes = volumes;
    }

    public int getRoom() {
        return room;
    }

    @NonNull
    public List<Integer> getVolumes() {
        return volumes;
    }
}
