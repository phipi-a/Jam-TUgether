package de.pcps.jamtugether.timer;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.timer.base.BaseJamTimer;
import timber.log.Timber;

public class JamCountDownTimer extends BaseJamTimer {

    public JamCountDownTimer(long startMillis, long interval, @NonNull OnTickCallback callback) {
        super(startMillis, 0, -interval, callback);
        Timber.d("JamCountDownTimer()");
    }

    @Override
    public void start() {
        super.start();
        Timber.d("start()");
    }

    @Override
    public void stop() {
        super.stop();
        Timber.d("stop()");
    }
}
