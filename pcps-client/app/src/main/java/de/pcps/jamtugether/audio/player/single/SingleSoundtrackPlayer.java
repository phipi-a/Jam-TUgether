package de.pcps.jamtugether.audio.player.single;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.audio.player.base.SoundtrackPlayer;
import de.pcps.jamtugether.audio.player.base.SoundtrackPlayingThread;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;

/**
 * This player is responsible for playing every single soundtrack of the app
 * It keeps track of what thread is responsible for what soundtrack
 */
@Singleton
public class SingleSoundtrackPlayer extends SoundtrackPlayer {

    @NonNull
    private final HashMap<Integer, SingleSoundtrackPlayingThread> threadMap = new HashMap<>();

    @Inject
    public SingleSoundtrackPlayer() {
    }

    @Nullable
    @Override
    protected SoundtrackPlayingThread createThread(@NonNull Soundtrack soundtrack) {
        if (soundtrack instanceof SingleSoundtrack) {
            SingleSoundtrack singleSoundtrack = (SingleSoundtrack) soundtrack;
            SingleSoundtrackPlayingThread thread = new SingleSoundtrackPlayingThread(singleSoundtrack);
            threadMap.put(singleSoundtrack.getUserID(), thread);
            return thread;
        }
        return null;
    }

    @Nullable
    @Override
    protected SoundtrackPlayingThread getThread(@NonNull Soundtrack soundtrack) {
        if (soundtrack instanceof SingleSoundtrack) {
            SingleSoundtrack singleSoundtrack = (SingleSoundtrack) soundtrack;
            SoundtrackPlayingThread thread = threadMap.get(singleSoundtrack.getUserID());
            return thread != null ? thread : createThread(soundtrack);
        }
        return null;
    }

    @Override
    public void stop(@NonNull Soundtrack soundtrack) {
        super.stop(soundtrack);
        if (soundtrack instanceof SingleSoundtrack) {
            SingleSoundtrack singleSoundtrack = (SingleSoundtrack) soundtrack;
            threadMap.remove(singleSoundtrack.getUserID());
        }
    }

    @Override
    public void stop() {
        for(SingleSoundtrackPlayingThread thread : threadMap.values()) {
            thread.stopThread();
        }
        threadMap.clear();
    }
}
