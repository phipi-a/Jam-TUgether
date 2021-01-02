package de.pcps.jamtugether.audio.sound;

import android.media.SoundPool;

import androidx.annotation.NonNull;

public class StopSoundThread extends Thread {

    @NonNull
    private final SoundPool soundPool;

    private final int streamID;

    public StopSoundThread(@NonNull SoundPool soundPool, int streamID) {
        this.soundPool = soundPool;
        this.streamID = streamID;
    }

    @Override
    public void run() {
        soundPool.stop(streamID);
    }

    public void stopSound() {
        start();
    }
}
