package de.pcps.jamtugether.audio.metronome;

import androidx.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MetronomePlayer {

    @Nullable
    private MetronomePlayingThread thread;

    private boolean active = true;

    @Inject
    public MetronomePlayer() { }

    public void play() {
        if(thread != null) {
            thread.stopMetronome();
            thread = null;
        }
        thread = new MetronomePlayingThread(active);
        thread.play();
    }

    public void setOnTickCallback(@Nullable MetronomePlayingThread.OnTickCallback onTickCallback) {
        if(thread != null) {
            thread.setOnTickCallback(onTickCallback);
        }
    }

    public void setActive(boolean active) {
        this.active = active;
        if(thread != null) {
            thread.setActive(active);
        }
    }

    public void stop() {
        if(thread != null) {
            thread.stopMetronome();
            thread = null;
        }
    }

    public boolean isActive() {
        return active;
    }
}
