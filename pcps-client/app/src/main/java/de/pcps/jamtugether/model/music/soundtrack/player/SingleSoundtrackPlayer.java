package de.pcps.jamtugether.model.music.soundtrack.player;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.model.instrument.base.Instruments;
import de.pcps.jamtugether.model.music.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.music.soundtrack.runnable.SingleSoundtrackRunnable;
import de.pcps.jamtugether.model.music.soundtrack.base.Soundtrack;

@Singleton
public class SingleSoundtrackPlayer {

    private final HashMap<Integer, SingleSoundtrackRunnable> runnableMap = new HashMap<>();

    @NonNull
    private final Instruments instruments;

    @Inject
    public SingleSoundtrackPlayer(@NonNull Instruments instruments) {
        this.instruments = instruments;
    }

    public void changeVolume(@NonNull SingleSoundtrack soundtrack, float volume) {
        SingleSoundtrackRunnable runnable = getRunnable(soundtrack) != null ? getRunnable(soundtrack) : createRunnable(soundtrack);
        runnable.setVolume(volume);
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
        SingleSoundtrackRunnable runnable = createRunnable(soundtrack);
        runnable.start();
    }

    private void pause(@NonNull SingleSoundtrack soundtrack) {
        SingleSoundtrackRunnable runnable = getRunnable(soundtrack);
        if (runnable != null) {
            runnable.pause();
        }
    }

    private void resume(@NonNull SingleSoundtrack soundtrack) {
        SingleSoundtrackRunnable runnable = getRunnable(soundtrack);
        if (runnable != null) {
            runnable.resume();
        }
    }

    public void fastForward(@NonNull SingleSoundtrack soundtrack) {
        SingleSoundtrackRunnable runnable = getRunnable(soundtrack);
        if (runnable != null) {
            runnable.fastForward();
        }
    }

    public void fastRewind(@NonNull SingleSoundtrack soundtrack) {
        SingleSoundtrackRunnable runnable = getRunnable(soundtrack);
        if (runnable != null) {
            runnable.fastRewind();
        }
    }

    public void stop(@NonNull SingleSoundtrack soundtrack) {
        SingleSoundtrackRunnable runnable = getRunnable(soundtrack);
        if (runnable != null) {
            runnable.stop();
        }
    }

    @Nullable
    private SingleSoundtrackRunnable getRunnable(@NonNull SingleSoundtrack soundtrack) {
        return runnableMap.get(soundtrack.getUserID());
    }

    @NonNull
    private SingleSoundtrackRunnable createRunnable(@NonNull SingleSoundtrack soundtrack) {
        SingleSoundtrackRunnable runnable = new SingleSoundtrackRunnable(soundtrack, instruments);
        runnableMap.put(soundtrack.getUserID(), runnable);
        return runnable;
    }
}
