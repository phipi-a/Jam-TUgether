package de.pcps.jamtugether.audio.sound.model;

/**
 * simple helper class to encapsulate stream id with according start time
 */
public class StreamID {

    private final int streamID;
    private final int startTime;

    public StreamID(int streamID, int startTime) {
        this.streamID = streamID;
        this.startTime = startTime;
    }

    public int getStreamID() {
        return streamID;
    }

    public int getStartTime() {
        return startTime;
    }
}
