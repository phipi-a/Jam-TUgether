package de.pcps.jamtugether.timer.base;

import android.os.Handler;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.utils.TimeUtils;

public abstract class BaseJamTimer implements Runnable {

    private final long startMillis;
    private final long stopMillis;
    private final long interval;

    @NonNull
    private final Handler handler = new Handler();

    @NonNull
    private final OnTickCallback callback;

    private long currentMillis;

    private boolean stopped;

    public BaseJamTimer(long startMillis, long stopMillis, long interval, @NonNull OnTickCallback callback) {
        this.startMillis = startMillis;
        this.stopMillis = stopMillis;
        this.interval = interval;
        this.callback = callback;
    }

    @Override
    public void run() {
        if (stopped) {
            return;
        }
        callback.onTicked(currentMillis);
        if (currentMillis == stopMillis) {
            stop();
            callback.onFinished();
        }
        currentMillis += interval;

        handler.postDelayed(this, TimeUtils.ONE_SECOND);
    }

    public void start() {
        stopped = false;
        currentMillis = startMillis;
        run();
    }

    public void stop() {
        stopped = true;
    }

    public void reset() {
        currentMillis = startMillis;
    }

    public boolean isStopped() {
        return stopped;
    }

    public interface OnTickCallback {

        void onTicked(long millis);

        void onFinished();
    }
}
