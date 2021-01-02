package de.pcps.jamtugether.audio.sound;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.audio.sound.pool.base.BaseSoundPool;

public class PlaySoundThread extends Thread {

    @NonNull
    private final BaseSoundPool soundPool;

    private final int soundID;
    private final float pitch;

    @NonNull
    private final OnSoundPlayedCallback callback;

    public PlaySoundThread(@NonNull BaseSoundPool soundPool, int soundID, float pitch, @NonNull OnSoundPlayedCallback callback) {
        this.soundPool = soundPool;
        this.soundID = soundID;
        this.pitch = pitch;
        this.callback = callback;
    }

    @Override
    public void run() {
        int streamID = soundPool.play(soundID, pitch);
        callback.onSoundPlayed(streamID);
    }

    public void playSound() {
        start();
    }
}
