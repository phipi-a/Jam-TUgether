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

    private long startTimeMillis = -1;
    private int lastMillis = -1;

    @NonNull
    private final Soundtrack soundtrack;

    public SoundtrackPlayingThread(@NonNull Soundtrack soundtrack) {
        this.soundtrack = soundtrack;
    }

    @Override
    public void run() {
        while (!paused && !stopped) {
            if (startTimeMillis == -1) {
                startTimeMillis = System.currentTimeMillis();
            }
            int millis = (int) (System.currentTimeMillis() - startTimeMillis);
            if (millis == lastMillis) {
                continue;
            }
            this.lastMillis = millis;
            play(millis);
            if (soundtrack.getJustResumed()) {
                soundtrack.setJustResumed(false);
            }
            if (millis == soundtrack.getLength()) {
                soundtrack.postState(Soundtrack.State.IDLE);
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
        paused = false;
        soundtrack.setJustResumed(true);
        soundtrack.postState(Soundtrack.State.PLAYING);
    }

    public void fastForward() {
        stopSound();
        int progressInMillis = this.progressInMillis + FAST_FORWARD_OFFSET;
        if (progressInMillis > soundtrack.getLength()) {
            return;
        }
        this.progressInMillis = progressInMillis;
        soundtrack.postProgress(progressInMillis / soundtrack.getLength() * 100);
    }

    public void fastRewind() {
        stopSound();
        int progressInMillis = this.progressInMillis + FAST_REWIND_OFFSET;
        if (progressInMillis < 0) {
            return;
        }
        this.progressInMillis = progressInMillis;
        soundtrack.postProgress(progressInMillis / soundtrack.getLength() * 100);
    }

    public void stopSoundtrack() {
        stopSound();
        soundtrack.postProgress(0);
        stopped = true;
        soundtrack.postState(Soundtrack.State.STOPPED);
    }

    protected abstract void stopSound();
}
