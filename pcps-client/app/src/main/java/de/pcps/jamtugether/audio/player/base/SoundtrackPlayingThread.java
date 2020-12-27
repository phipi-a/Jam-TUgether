package de.pcps.jamtugether.audio.player.base;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;

import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.model.sound.SoundWithStreamID;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.utils.TimeUtils;

public abstract class SoundtrackPlayingThread extends Thread {

    private static final int FAST_FORWARD_OFFSET = (int) TimeUtils.ONE_SECOND;
    private static final int FAST_REWIND_OFFSET = (int) -TimeUtils.ONE_SECOND;

    private boolean paused = false;
    private boolean stopped = false;

    private int progressInMillis = 0;
    private long lastMillis = -1;

    @NonNull
    private final Soundtrack soundtrack;

    /**
     * maps a sound to the given stream id
     */
    protected final HashMap<Sound, Integer> streamIDsMap;

    public SoundtrackPlayingThread(@NonNull Soundtrack soundtrack) {
        this.soundtrack = soundtrack;
        this.streamIDsMap = new HashMap<>();
    }

    @Override
    public void run() {
        while (!stopped) {
            if(paused) {
                continue;
            }
            if (progressInMillis == soundtrack.getLength()) {
                soundtrack.postState(Soundtrack.State.IDLE);
                stopAllSounds();
                // soundtrack finished playing
                break;
            }
            long millis = System.currentTimeMillis();
            if (millis == lastMillis) {
                continue;
            }
            if(lastMillis == -1) {
                this.progressInMillis = 0;
            } else {
                this.progressInMillis += (int) (millis - lastMillis);
            }
            this.lastMillis = millis;
            soundtrack.postProgress(calculateProgress(progressInMillis));
            List<SoundWithStreamID> soundsWithStreamIDs = play(progressInMillis);
            for(SoundWithStreamID soundWithStreamID : soundsWithStreamIDs) {
                streamIDsMap.put(soundWithStreamID.getSound(), soundWithStreamID.getStreamID());
            }
            stopSounds(progressInMillis);
            if (soundtrack.getJustResumed()) {
                soundtrack.setJustResumed(false);
            }
        }
    }

    /**
     * plays all sounds that start at the given time
     * resumes sounds if necessary
     * returns a list of sounds with their corresponding streaming ids from the sounds that are playing
     */
    public abstract List<SoundWithStreamID> play(int millis);

    public void start() {
        super.start();
        soundtrack.postProgress(0);
        soundtrack.postState(Soundtrack.State.PLAYING);
    }

    public void pause() {
        stopAllSounds();
        paused = true;
        soundtrack.postState(Soundtrack.State.PAUSED);
    }

    public void resumeSoundtrack() {
        lastMillis = System.currentTimeMillis();
        paused = false;
        soundtrack.setJustResumed(true);
        soundtrack.postState(Soundtrack.State.PLAYING);
    }

    public void fastForward() {
        stopAllSounds();
        int progressInMillis = this.progressInMillis + FAST_FORWARD_OFFSET;
        this.progressInMillis = Math.min(progressInMillis, soundtrack.getLength());
        soundtrack.postProgress(calculateProgress(this.progressInMillis));
    }

    public void fastRewind() {
        stopAllSounds();
        int progressInMillis = this.progressInMillis + FAST_REWIND_OFFSET;
        this.progressInMillis = Math.max(progressInMillis, 0);
        soundtrack.postProgress(calculateProgress(this.progressInMillis));
    }

    public void stopSoundtrack() {
        stopAllSounds();
        soundtrack.postProgress(0);
        stopped = true;
        soundtrack.postState(Soundtrack.State.STOPPED);
    }

    private int calculateProgress(int millis) {
        return (int) ((millis / (float) soundtrack.getLength()) * 100);
    }

    protected abstract void setVolume(float volume);

    protected abstract void stopSounds(int millis);

    protected abstract void stopAllSounds();
}
