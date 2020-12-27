package de.pcps.jamtugether.audio.player.composite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.audio.player.base.SoundtrackPlayer;
import de.pcps.jamtugether.audio.player.base.SoundtrackPlayingThread;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;

/**
 * This player is responsible for playing every single soundtrack of the app
 * It keeps track of what thread is responsible for what soundtrack
 */
@Singleton
public class CompositeSoundtrackPlayer extends SoundtrackPlayer {

    @NonNull
    protected final HashMap<List<Integer>, SoundtrackPlayingThread> threadMap = new HashMap<>();

    @Inject
    public CompositeSoundtrackPlayer() { }

    @Nullable
    @Override
    protected SoundtrackPlayingThread getThread(@NonNull Soundtrack soundtrack) {
        if(soundtrack instanceof CompositeSoundtrack) {
            CompositeSoundtrack compositeSoundtrack = (CompositeSoundtrack) soundtrack;
            SoundtrackPlayingThread thread = threadMap.get(compositeSoundtrack.getUserIDs());
            return thread != null ? thread : createThread(soundtrack);
        }
        return null;
    }

    @Nullable
    @Override
    protected SoundtrackPlayingThread createThread(@NonNull Soundtrack soundtrack) {
        if(soundtrack instanceof CompositeSoundtrack) {
            CompositeSoundtrack compositeSoundtrack = (CompositeSoundtrack) soundtrack;
            CompositeSoundtrackPlayingThread thread = new CompositeSoundtrackPlayingThread(compositeSoundtrack);
            threadMap.put(compositeSoundtrack.getUserIDs(), thread);
            return thread;
        }
        return null;
    }

    @Override
    public void stop(@NonNull Soundtrack soundtrack) {
        super.stop(soundtrack);
        if(soundtrack instanceof CompositeSoundtrack) {
            CompositeSoundtrack compositeSoundtrack = (CompositeSoundtrack) soundtrack;
            threadMap.remove(compositeSoundtrack.getUserIDs());
        }
    }
}
