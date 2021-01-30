package de.pcps.jamtugether.audio.metronome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.model.beat.Beat;

@Singleton
public class MetronomePlayer {

    @Nullable
    private MetronomePlayingThread thread;

    @Inject
    public MetronomePlayer() { }

    public void play() {
        if(thread != null) {
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

    public void onBeatChanged(@NonNull Beat beat) {
        if(thread != null) {
            thread.stopMetronome();
            Metronome.getInstance().updateBeat(beat);
            thread = null;
        }
    }
}
