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
    private final HashMap<String, SingleSoundtrackPlayingThread> threadMap = new HashMap<>();

    @Inject
    public SingleSoundtrackPlayer() {
    }

    @Nullable
    @Override
    protected SoundtrackPlayingThread createThread(@NonNull Soundtrack soundtrack) {
        if (soundtrack instanceof SingleSoundtrack) {
            SingleSoundtrack singleSoundtrack = (SingleSoundtrack) soundtrack;
            SingleSoundtrackPlayingThread thread = new SingleSoundtrackPlayingThread(singleSoundtrack, this);
            threadMap.put(singleSoundtrack.getID(), thread);
            return thread;
        }
        return null;
    }

    @Nullable
    @Override
    protected SoundtrackPlayingThread getThread(@NonNull Soundtrack soundtrack) {
        if (soundtrack instanceof SingleSoundtrack) {
            SingleSoundtrack singleSoundtrack = (SingleSoundtrack) soundtrack;
            SoundtrackPlayingThread thread = threadMap.get(singleSoundtrack.getID());
            return thread != null ? thread : createThread(soundtrack);
        }
        return null;
    }

    @Override
    public boolean isPlaying() {
        for (SingleSoundtrackPlayingThread thread : threadMap.values()) {
            if (thread.isPlaying()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void pause(@NonNull Soundtrack soundtrack) {
        super.pause(soundtrack);
        if (soundtrack instanceof SingleSoundtrack) {
            SingleSoundtrack singleSoundtrack = (SingleSoundtrack) soundtrack;
            threadMap.remove(singleSoundtrack.getID());
        }
    }

    @Override
    public void stop(@NonNull Soundtrack soundtrack) {
        super.stop(soundtrack);
        if (soundtrack instanceof SingleSoundtrack) {
            SingleSoundtrack singleSoundtrack = (SingleSoundtrack) soundtrack;
            threadMap.remove(singleSoundtrack.getID());
        }
    }

    @Override
    public void stop() {
        for (SingleSoundtrackPlayingThread thread : threadMap.values()) {
            thread.stopSoundtrack();
        }
        threadMap.clear();
    }

    @Override
    public void onSoundtrackFinished(@NonNull SoundtrackPlayingThread thread) {
        thread.stopSoundtrack();
        Soundtrack soundtrack = thread.getSoundtrack();
        if (soundtrack instanceof SingleSoundtrack) {
            SingleSoundtrack singleSoundtrack = (SingleSoundtrack) soundtrack;
            threadMap.remove(singleSoundtrack.getID());
        }
    }
}
