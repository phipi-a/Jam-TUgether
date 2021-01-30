package de.pcps.jamtugether.audio.metronome;

import androidx.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class MetronomePlayer {

    @Nullable
    private MetronomePlayingThread thread;

    @Inject
    public MetronomePlayer() { }

    public void play() {
        if(thread != null) {
            Timber.d("thread != null");
            thread.stopMetronome();
            thread = null;
        }
        thread = new MetronomePlayingThread();
        thread.play();
    }

    public void stop() {
        if(thread != null) {
            thread.stopMetronome();
            thread = null;
        }
    }
}
