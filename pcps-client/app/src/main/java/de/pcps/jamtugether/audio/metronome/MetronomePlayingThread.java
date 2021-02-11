package de.pcps.jamtugether.audio.metronome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.Beat;

public class MetronomePlayingThread extends Thread {

    private boolean running;
    private boolean stopped;

    private long progressInMillis;
    private long lastMillis = -1;
    private long lastProgressInMillis = -1;

    @NonNull
    private static final Metronome metronome = Metronome.getInstance();

    @Nullable
    private OnTickCallback onTickCallback;

    private boolean active;

    public MetronomePlayingThread(boolean active) {
        this.active = active;
    }

    public void setOnTickCallback(@Nullable OnTickCallback onTickCallback) {
        this.onTickCallback = onTickCallback;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void run() {
        Beat beat = metronome.getBeat();

        while (!stopped) {
            if (progressInMillis != lastProgressInMillis) {
                if (progressInMillis % beat.getMillisPerTact() == 0) {
                    if (active) {
                        metronome.playSound(R.raw.metronome_up);
                    }
                    if (onTickCallback != null) {
                        onTickCallback.onNewTactTick(progressInMillis);
                        onTickCallback.onTick(progressInMillis);
                    }
                } else if (progressInMillis % (beat.getMillisPerTact() / beat.getTicksPerTact()) == 0) {
                    if (active) {
                        metronome.playSound(R.raw.metronome);
                    }
                    if (onTickCallback != null) {
                        onTickCallback.onTick(progressInMillis);
                    }
                }
                lastProgressInMillis = progressInMillis;
            }

            long millis = System.currentTimeMillis();
            this.progressInMillis += (int) (millis - lastMillis);
            this.lastMillis = millis;
        }
    }

    public void play() {
        lastMillis = System.currentTimeMillis();
        if (!running) {
            super.start();
            running = true;
        }
    }

    public void stopMetronome() {
        stopped = true;
        metronome.stop();
    }

    public interface OnTickCallback {

        void onNewTactTick(long millis);

        void onTick(long millis);
    }
}
