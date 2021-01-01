package de.pcps.jamtugether.timer;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.timer.base.BaseJamTimer;

public class JamCountDownTimer extends BaseJamTimer {
    
    public JamCountDownTimer(long startMillis, long interval, @NonNull OnTickCallback callback) {
        super(startMillis, 0, -interval, callback);
    }
}
