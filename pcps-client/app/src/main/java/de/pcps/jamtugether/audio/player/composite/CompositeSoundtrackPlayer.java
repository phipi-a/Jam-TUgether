package de.pcps.jamtugether.audio.player.composite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.audio.instrument.base.Instruments;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;

@Singleton
public class CompositeSoundtrackPlayer {

    private final HashMap<List<Integer>, CompositeSoundtrackPlayingThread> threadMap = new HashMap<>();

    @NonNull
    private final Instruments instruments;

    @Inject
    public CompositeSoundtrackPlayer(@NonNull Instruments instruments) {
        this.instruments = instruments;
    }

    public void changeVolume(@NonNull CompositeSoundtrack soundtrack, float volume) {
        CompositeSoundtrackPlayingThread thread = getThread(soundtrack) != null ? getThread(soundtrack) : createThread(soundtrack);
        thread.setVolume(volume);
    }

    public void playOrPause(@NonNull CompositeSoundtrack soundtrack) {
        if (soundtrack.getSoundtracks().isEmpty()) {
            return;
        }

        Soundtrack.State state = soundtrack.getState().getValue();

        if (state == null) {
            return;
        }

        switch (state) {
            case PLAYING:
                pause(soundtrack);
                break;
            case PAUSED:
                resume(soundtrack);
                break;
            case IDLE:
            case STOPPED:
                play(soundtrack);
        }
    }

    private void play(@NonNull CompositeSoundtrack soundtrack) {
        CompositeSoundtrackPlayingThread thread = createThread(soundtrack);
        thread.start();
    }

    private void pause(@NonNull CompositeSoundtrack soundtrack) {
        CompositeSoundtrackPlayingThread thread = getThread(soundtrack);
        if (thread != null) {
            thread.pause();
        }
    }

    private void resume(@NonNull CompositeSoundtrack soundtrack) {
        CompositeSoundtrackPlayingThread thread = getThread(soundtrack);
        if (thread != null) {
            thread.resumeSoundtrack();
        }
    }

    public void fastForward(@NonNull CompositeSoundtrack soundtrack) {
        CompositeSoundtrackPlayingThread thread = getThread(soundtrack);
        if (thread != null) {
            thread.fastForward();
        }
    }

    public void fastRewind(@NonNull CompositeSoundtrack soundtrack) {
        CompositeSoundtrackPlayingThread thread = getThread(soundtrack);
        if (thread != null) {
            thread.fastRewind();
        }
    }

    public void stop(@NonNull CompositeSoundtrack soundtrack) {
        CompositeSoundtrackPlayingThread thread = getThread(soundtrack);
        if (thread != null) {
            thread.stopSoundtrack();
        }
    }

    @Nullable
    private CompositeSoundtrackPlayingThread getThread(@NonNull CompositeSoundtrack soundtrack) {
        return threadMap.get(soundtrack.getUserIDs());
    }

    @NonNull
    private CompositeSoundtrackPlayingThread createThread(@NonNull CompositeSoundtrack soundtrack) {
        CompositeSoundtrackPlayingThread thread = new CompositeSoundtrackPlayingThread(soundtrack, instruments);
        threadMap.put(soundtrack.getUserIDs(), thread);
        return thread;
    }
}
