package de.pcps.jamtugether.audio.sound;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.audio.sound.model.SoundWithStreamID;

public class PlaySoundThread extends Thread {

    @NonNull
    private final BaseSoundPool soundPool;

    private final int soundID;

    @NonNull
    private final OnSoundPlayedCallback callback;

    public PlaySoundThread(@NonNull BaseSoundPool soundPool, int soundID, @NonNull OnSoundPlayedCallback callback) {
        this.soundPool = soundPool;
        this.soundID = soundID;
        this.callback = callback;
    }

    @Override
    public void run() {
        int streamID = soundPool.play(soundID);
        callback.onSoundPlayed(streamID);
    }

    public void playSound() {
        start();
    }

    public interface OnSoundPlayedCallback {

        void onSoundPlayed(int streamID);
    }

    public interface OnSoundWithIDPlayedCallback {

        void onSoundPlayed(@NonNull SoundWithStreamID soundWithStreamID);
    }
}
