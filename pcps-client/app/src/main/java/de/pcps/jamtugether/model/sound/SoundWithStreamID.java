package de.pcps.jamtugether.model.sound;

import androidx.annotation.NonNull;

/**
 * simple helper class to encapsulate sound with according stream id
 */
public class SoundWithStreamID {

    @NonNull
    private final Sound sound;

    private final int streamID;

    public SoundWithStreamID(@NonNull Sound sound, int streamID) {
        this.sound = sound;
        this.streamID = streamID;
    }

    @NonNull
    public Sound getSound() {
        return sound;
    }

    public int getStreamID() {
        return streamID;
    }
}


