package de.pcps.jamtugether.audio.metronome;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.model.beat.Beat;

public class MetronomePlayingThread extends Thread {

    private boolean running;
    private boolean stopped;

    private long progressInMillis;
    private long lastMillis = -1;

    @NonNull
    private static final Metronome metronome = Metronome.getInstance();

    @NonNull
    private final Beat beat = metronome.getBeat();

    @Override
    public void run() {
        while(!stopped) {

            // todo check milli seconds and play new sound if necessary
            //  check SoundtrackPlayingThread
        }
    }

    public void play() {
        lastMillis = System.currentTimeMillis();
        if (!running) {
            super.start();
            running = true;
        }
        metronome.postPlaying(true);
    }

    public void stopMetronome() {
        stopped = true;
        metronome.stop();
        metronome.postPlaying(false);
    }
}
