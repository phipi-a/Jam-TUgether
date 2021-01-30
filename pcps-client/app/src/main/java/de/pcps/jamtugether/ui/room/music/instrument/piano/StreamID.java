package de.pcps.jamtugether.ui.room.music.instrument.piano;

public class StreamID {

    private final int id;
    private final int startTime;

    public StreamID(int id, int startTime) {
        this.id = id;
        this.startTime = startTime;
    }

    public int getID() {
        return id;
    }

    public int getStartTime() {
        return startTime;
    }
}
