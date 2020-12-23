package de.pcps.jamtugether.model.music.soundtrack.player;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.model.instrument.base.Instruments;
import de.pcps.jamtugether.model.music.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.music.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.model.music.soundtrack.runnable.CompositeSoundtrackRunnable;

@Singleton
public class CompositeSoundtrackPlayer {

    private final HashMap<List<Integer>, CompositeSoundtrackRunnable> runnableMap = new HashMap<>();

    @NonNull
    private final Instruments instruments;

    @Inject
    public CompositeSoundtrackPlayer(@NonNull Instruments instruments) {
        this.instruments = instruments;
    }

    public void changeVolume(@NonNull CompositeSoundtrack soundtrack, float volume) {
        CompositeSoundtrackRunnable runnable = getRunnable(soundtrack) != null ? getRunnable(soundtrack) : createRunnable(soundtrack);
        runnable.setVolume(volume);
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
        CompositeSoundtrackRunnable runnable = createRunnable(soundtrack);
        runnable.start();
    }

    private void pause(@NonNull CompositeSoundtrack soundtrack) {
        CompositeSoundtrackRunnable runnable = getRunnable(soundtrack);
        if (runnable != null) {
            runnable.pause();
        }
    }

    private void resume(@NonNull CompositeSoundtrack soundtrack) {
        CompositeSoundtrackRunnable runnable = getRunnable(soundtrack);
        if (runnable != null) {
            runnable.resume();
        }
    }

    public void fastForward(@NonNull CompositeSoundtrack soundtrack) {
        CompositeSoundtrackRunnable runnable = getRunnable(soundtrack);
        if (runnable != null) {
            runnable.fastForward();
        }
    }

    public void fastRewind(@NonNull CompositeSoundtrack soundtrack) {
        CompositeSoundtrackRunnable runnable = getRunnable(soundtrack);
        if (runnable != null) {
            runnable.fastRewind();
        }
    }

    public void stop(@NonNull CompositeSoundtrack soundtrack) {
        CompositeSoundtrackRunnable runnable = getRunnable(soundtrack);
        if (runnable != null) {
            runnable.stop();
        }
    }

    @Nullable
    private CompositeSoundtrackRunnable getRunnable(@NonNull CompositeSoundtrack soundtrack) {
        return runnableMap.get(soundtrack.getUserIDs());
    }

    @NonNull
    private CompositeSoundtrackRunnable createRunnable(@NonNull CompositeSoundtrack soundtrack) {
        CompositeSoundtrackRunnable runnable = new CompositeSoundtrackRunnable(soundtrack, instruments);
        runnableMap.put(soundtrack.getUserIDs(), runnable);
        return runnable;
    }
}
