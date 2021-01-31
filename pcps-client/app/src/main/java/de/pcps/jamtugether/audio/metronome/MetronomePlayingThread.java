package de.pcps.jamtugether.audio.metronome;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.beat.Beat;
import timber.log.Timber;

public class MetronomePlayingThread extends Thread {

    private boolean running;
    private boolean stopped;

    private long progressInMillis;
    private long lastMillis = -1;
    private long lastProgressInMillis = -1;

    @NonNull
    private static final Metronome metronome = Metronome.getInstance();

    @Override
    public void run() {
        Timber.d("run()");
        Beat beat = metronome.getBeat();

        while (!stopped) {
            if (progressInMillis != lastProgressInMillis) {
                Timber.d("millis per tact: %d", beat.getMillisPerTact());
                if (progressInMillis % beat.getMillisPerTact() == 0) {
                    metronome.playSound(R.raw.metronome_up);
                } else if (progressInMillis % (beat.getMillisPerTact() / beat.getTicksPerTact()) == 0) {
                    metronome.playSound(R.raw.metronome);
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
}
