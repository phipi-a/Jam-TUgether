package de.pcps.jamtugether.audio.player.single;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.audio.instrument.base.Instruments;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;

@Singleton
public class SingleSoundtrackPlayer {

    private final HashMap<Integer, SingleSoundtrackPlayingThread> threadMap = new HashMap<>();

    @NonNull
    private final Instruments instruments;

    @Inject
    public SingleSoundtrackPlayer(@NonNull Instruments instruments) {
        this.instruments = instruments;
    }

    public void changeVolume(@NonNull SingleSoundtrack soundtrack, float volume) {
        SingleSoundtrackPlayingThread thread = getThread(soundtrack) != null ? getThread(soundtrack) : createThread(soundtrack);
        thread.setVolume(volume);
    }

    public void playOrPause(@NonNull SingleSoundtrack soundtrack) {
        if (soundtrack.getSoundSequence().isEmpty()) {
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

    private void play(@NonNull SingleSoundtrack soundtrack) {
        SingleSoundtrackPlayingThread thread = createThread(soundtrack);
        thread.start();
    }

    private void pause(@NonNull SingleSoundtrack soundtrack) {
        SingleSoundtrackPlayingThread thread = getThread(soundtrack);
        if (thread != null) {
            thread.pause();
        }
    }

    private void resume(@NonNull SingleSoundtrack soundtrack) {
        SingleSoundtrackPlayingThread thread = getThread(soundtrack);
        if (thread != null) {
            thread.resumeSoundtrack();
        }
    }

    public void fastForward(@NonNull SingleSoundtrack soundtrack) {
        SingleSoundtrackPlayingThread thread = getThread(soundtrack);
        if (thread != null) {
            thread.fastForward();
        }
    }

    public void fastRewind(@NonNull SingleSoundtrack soundtrack) {
        SingleSoundtrackPlayingThread thread = getThread(soundtrack);
        if (thread != null) {
            thread.fastRewind();
        }
    }

    public void stop(@NonNull SingleSoundtrack soundtrack) {
        SingleSoundtrackPlayingThread thread = getThread(soundtrack);
        if (thread != null) {
            thread.stopSoundtrack();
        }
    }

    @Nullable
    private SingleSoundtrackPlayingThread getThread(@NonNull SingleSoundtrack soundtrack) {
        return threadMap.get(soundtrack.getUserID());
    }

    @NonNull
    private SingleSoundtrackPlayingThread createThread(@NonNull SingleSoundtrack soundtrack) {
        SingleSoundtrackPlayingThread thread = new SingleSoundtrackPlayingThread(soundtrack, instruments);
        threadMap.put(soundtrack.getUserID(), thread);
        return thread;
    }
}
