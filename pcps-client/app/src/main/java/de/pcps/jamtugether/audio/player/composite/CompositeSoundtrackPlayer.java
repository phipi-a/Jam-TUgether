package de.pcps.jamtugether.audio.player.composite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.audio.player.base.SoundtrackPlayer;
import de.pcps.jamtugether.audio.player.base.SoundtrackPlayingThread;
import de.pcps.jamtugether.audio.player.single.SingleSoundtrackPlayingThread;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import timber.log.Timber;

/**
 * This player is responsible for playing every single soundtrack of the app
 * It keeps track of what thread is responsible for what soundtrack
 */
@Singleton
public class CompositeSoundtrackPlayer extends SoundtrackPlayer {

    @NonNull
    private final HashMap<List<Integer>, CompositeSoundtrackPlayingThread> threadMap = new HashMap<>();

    @Inject
    public CompositeSoundtrackPlayer() {
    }

    @Nullable
    @Override
    protected SoundtrackPlayingThread createThread(@NonNull Soundtrack soundtrack) {
        if (soundtrack instanceof CompositeSoundtrack) {
            CompositeSoundtrack compositeSoundtrack = (CompositeSoundtrack) soundtrack;
            CompositeSoundtrackPlayingThread thread = new CompositeSoundtrackPlayingThread(compositeSoundtrack, this);
            threadMap.put(compositeSoundtrack.getUserIDs(), thread);
            return thread;
        }
        return null;
    }

    @Nullable
    @Override
    protected SoundtrackPlayingThread getThread(@NonNull Soundtrack soundtrack) {
        if (soundtrack instanceof CompositeSoundtrack) {
            CompositeSoundtrack compositeSoundtrack = (CompositeSoundtrack) soundtrack;
            CompositeSoundtrackPlayingThread thread = threadMap.get(compositeSoundtrack.getUserIDs());
            return thread != null ? thread : createThread(soundtrack);
        }
        return null;
    }

    @Override
    public boolean isPlaying() {
        for(CompositeSoundtrackPlayingThread thread : threadMap.values()) {
            if(thread.isPlaying()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void pause(@NonNull Soundtrack soundtrack) {
        super.pause(soundtrack);
        if (soundtrack instanceof CompositeSoundtrack) {
            CompositeSoundtrack compositeSoundtrack = (CompositeSoundtrack) soundtrack;
            threadMap.remove(compositeSoundtrack.getUserIDs());
        }
    }

    @Override
    public void stop(@NonNull Soundtrack soundtrack) {
        super.stop(soundtrack);
        if (soundtrack instanceof CompositeSoundtrack) {
            CompositeSoundtrack compositeSoundtrack = (CompositeSoundtrack) soundtrack;
            threadMap.remove(compositeSoundtrack.getUserIDs());
        }
    }

    @Override
    public void stop() {
        for(SoundtrackPlayingThread thread : threadMap.values()) {
            thread.stopSoundtrack();
        }
        threadMap.clear();
    }

    @Override
    public void onSoundtrackFinished(@NonNull SoundtrackPlayingThread thread) {
        thread.stopSoundtrack();
        Soundtrack soundtrack = thread.getSoundtrack();
        if(soundtrack instanceof CompositeSoundtrack) {
            CompositeSoundtrack compositeSoundtrack = (CompositeSoundtrack) soundtrack;
            threadMap.remove(compositeSoundtrack.getUserIDs());
        }
    }
}
