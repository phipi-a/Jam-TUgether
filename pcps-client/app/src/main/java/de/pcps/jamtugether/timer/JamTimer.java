package de.pcps.jamtugether.timer;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.timer.base.BaseJamTimer;

public class JamTimer extends BaseJamTimer {

    public JamTimer(long stopMillis, long interval, @NonNull OnTickCallback callback) {
        super(0, stopMillis, interval, callback);
    }
}
