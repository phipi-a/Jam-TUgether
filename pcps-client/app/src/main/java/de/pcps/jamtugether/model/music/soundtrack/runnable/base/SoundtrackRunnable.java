package de.pcps.jamtugether.model.music.soundtrack.runnable.base;

import android.os.Handler;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.model.music.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.utils.TimeUtils;

public abstract class SoundtrackRunnable implements Runnable{

    private static final int FAST_FORWARD_OFFSET = (int) TimeUtils.TEN_SECONDS;
    private static final int FAST_REWIND_OFFSET = (int) -TimeUtils.TEN_SECONDS;

    private static final int DELAY_TIME = 1;

    private boolean paused = false;
    private boolean stopped = false;

    private int progressInMillis = 0;

    @NonNull
    private final Soundtrack soundtrack;

    @NonNull
    private final Handler handler;

    public SoundtrackRunnable(@NonNull Soundtrack soundtrack) {
        this.soundtrack = soundtrack;
        this.handler = new Handler();
    }

    @Override
    public void run() {
        if (!paused && !stopped) {
            play(progressInMillis);
            progressInMillis++;
            handler.postDelayed(this, DELAY_TIME);
        }
    }

    public abstract void play(int millis);

    public void start() {
        handler.postDelayed(this, DELAY_TIME);
        soundtrack.setState(Soundtrack.State.PLAYING);
    }

    public void pause() {
        stopSound();
        paused = true;
        soundtrack.setState(Soundtrack.State.PAUSED);
    }

    public void resume() {
        paused = false;
        soundtrack.setState(Soundtrack.State.PLAYING);
    }

    public void fastForward() {
        stopSound();
        int progressInMillis = this.progressInMillis + FAST_FORWARD_OFFSET;
        if(progressInMillis > soundtrack.getLength()) {
            return;
        }
        setProgressInMillis(progressInMillis);
    }

    public void fastRewind() {
        stopSound();
        int progressInMillis = this.progressInMillis + FAST_REWIND_OFFSET;
        if(progressInMillis < 0) {
            return;
        }
        setProgressInMillis(progressInMillis);
    }

    public void stop() {
        stopSound();
        stopped = true;
        handler.removeCallbacks(this);
        soundtrack.setState(Soundtrack.State.STOPPED);
        setProgressInMillis(0);
    }

    protected abstract void stopSound();

    private void setProgressInMillis(int progressInMillis) {
        this.progressInMillis = progressInMillis;
        int progress = (int) (progressInMillis / (float) soundtrack.getLength() * 100);
        soundtrack.setProgress(progress);
    }
}
