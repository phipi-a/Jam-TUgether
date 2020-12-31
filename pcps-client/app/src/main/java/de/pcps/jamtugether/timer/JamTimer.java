package de.pcps.jamtugether.timer;

import android.os.Handler;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.utils.TimeUtils;

public class JamTimer implements Runnable {

    private int millis;

    private boolean stopped;

    @NonNull
    private final Handler handler = new Handler();

    @NonNull
    private final OnTickCallback callback;

    /**
     * timestamp at which timer stops
     */
    private final long stopMillis;

    public JamTimer(@NonNull OnTickCallback callback, long stopMillis) {
        this.callback = callback;
        this.stopMillis = stopMillis;
    }

    @Override
    public void run() {
        if(stopped) {
            return;
        }
        callback.onTicked(millis);
        if(millis == stopMillis) {
            callback.onFinished();
        }
        millis += TimeUtils.ONE_SECOND;

        handler.postDelayed(this, TimeUtils.ONE_SECOND);
    }

    public void start() {
        stopped = false;
        millis = 0;
        run();
    }

    public void stop() {
        stopped = true;
    }

    public interface OnTickCallback {

        void onTicked(long millis);

        void onFinished();
    }
}
