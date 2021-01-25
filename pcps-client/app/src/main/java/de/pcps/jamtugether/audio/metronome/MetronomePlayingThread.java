package de.pcps.jamtugether.audio.metronome;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.beat.Beat;

public class MetronomePlayingThread extends Thread {

    private boolean running;
    private boolean stopped;

    private long progressInMillis;
    private long lastMillis = -1;
    private long lastProgressInMillis;
    int counter = 0;

    @NonNull
    private static final Metronome metronome = Metronome.getInstance();

    @NonNull
    private final Beat beat = metronome.getBeat();

    @Override
    public void run() {
        while(!stopped) {
            metronome.play(R.raw.drum_cymbal);
            if(counter%beat.getTicksPerTact()==0) {
                counter++;
                lastProgressInMillis = progressInMillis;
                metronome.play(R.raw.metronome_up);
            }
            else if(progressInMillis%(beat.getMillisPerTact()/beat.getTicksPerTact())==0){
                if (progressInMillis != lastProgressInMillis) {
                    counter++;
                    lastProgressInMillis = progressInMillis;
                    metronome.play(R.raw.metronome);
                }
            }


            long millis = System.currentTimeMillis();
            this.progressInMillis += (int) (millis - lastMillis);
            //soundtrack.postProgressInMillis(progressInMillis);
            this.lastMillis = millis;

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
