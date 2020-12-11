package de.pcps.jamtugether.models.music.volume;

import androidx.annotation.NonNull;

import java.util.List;

public class VolumeComposition {

    private final int roomID;

    @NonNull
    private final List<Integer> volumeSequence;

    public VolumeComposition(int roomID, @NonNull List<Integer> volumeSequence) {
        this.roomID = roomID;
        this.volumeSequence = volumeSequence;
    }

    public int getRoomID() {
        return roomID;
    }

    @NonNull
    public List<Integer> getVolumeSequence() {
        return volumeSequence;
    }
}
