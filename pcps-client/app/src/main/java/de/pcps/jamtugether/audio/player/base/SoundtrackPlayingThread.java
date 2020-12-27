package de.pcps.jamtugether.audio.player.base;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.utils.TimeUtils;

public abstract class SoundtrackPlayingThread extends Thread {

    private static final int FAST_FORWARD_OFFSET = (int) TimeUtils.TEN_SECONDS;
    private static final int FAST_REWIND_OFFSET = (int) -TimeUtils.TEN_SECONDS;

    private boolean paused = false;
    private boolean stopped = false;

    private int progressInMillis = 0;
    private long lastMillis = -1;

    @NonNull
    private final Soundtrack soundtrack;

    public SoundtrackPlayingThread(@NonNull Soundtrack soundtrack) {
        this.soundtrack = soundtrack;
    }

    @Override
    public void run() {
        while (!stopped) {
            if(paused) {
                continue;
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
            play(progressInMillis);
            if (soundtrack.getJustResumed()) {
                soundtrack.setJustResumed(false);
            }
            if (progressInMillis == soundtrack.getLength()) {
                soundtrack.postState(Soundtrack.State.IDLE);
                stopSound();
                stopped = true;
            }
        }
    }

    public abstract void play(int millis);

    public void start() {
        super.start();
        soundtrack.postProgress(0);
        soundtrack.postState(Soundtrack.State.PLAYING);
    }

    public void pause() {
        stopSound();
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
        stopSound();
        int progressInMillis = this.progressInMillis + FAST_FORWARD_OFFSET;
        this.progressInMillis = Math.min(progressInMillis, soundtrack.getLength());
        soundtrack.postProgress(calculateProgress(progressInMillis));
    }

    public void fastRewind() {
        stopSound();
        int progressInMillis = this.progressInMillis + FAST_REWIND_OFFSET;
        this.progressInMillis = Math.max(progressInMillis, 0);
        soundtrack.postProgress(calculateProgress(progressInMillis));
    }

    public void stopSoundtrack() {
        stopSound();
        soundtrack.postProgress(0);
        stopped = true;
        soundtrack.postState(Soundtrack.State.STOPPED);
    }

    private int calculateProgress(int millis) {
        return millis / soundtrack.getLength() * 100;
    }

    protected abstract void setVolume(float volume);

    protected abstract void stopSound();
}
