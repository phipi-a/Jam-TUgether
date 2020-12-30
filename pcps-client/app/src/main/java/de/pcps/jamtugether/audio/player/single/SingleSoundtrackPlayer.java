package de.pcps.jamtugether.audio.player.single;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.audio.player.base.SoundtrackPlayer;
import de.pcps.jamtugether.audio.player.base.SoundtrackPlayingThread;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import timber.log.Timber;

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
            SingleSoundtrackPlayingThread thread = new SingleSoundtrackPlayingThread(singleSoundtrack, this);
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
    protected void pause(@NonNull Soundtrack soundtrack) {
        super.pause(soundtrack);
        if (soundtrack instanceof SingleSoundtrack) {
            SingleSoundtrack singleSoundtrack = (SingleSoundtrack) soundtrack;
            threadMap.remove(singleSoundtrack.getUserID());
        }
    }

    @Override
    public void stop(@NonNull Soundtrack soundtrack) {
        super.stop(soundtrack);
        if (soundtrack instanceof SingleSoundtrack) {
            SingleSoundtrack singleSoundtrack = (SingleSoundtrack) soundtrack;
            threadMap.remove(singleSoundtrack.getUserID());
        }
    }

    /**
     * stops every soundtrack except the ones that are in the given list
     */
    public void stopExcept(@NonNull List<SingleSoundtrack> keepPlayingList) {
        List<Integer> toBeRemoved = new ArrayList<>();
        for (Integer key : threadMap.keySet()) {
            SingleSoundtrackPlayingThread thread = threadMap.get(key);
            if(!keepPlaying(thread, keepPlayingList)) {
                thread.stopSoundtrack();
                toBeRemoved.add(key);
            }
        }
        for (Integer key : toBeRemoved) {
            threadMap.remove(key);
        }
    }

    private boolean keepPlaying(@NonNull SingleSoundtrackPlayingThread thread, @NonNull List<SingleSoundtrack> keepPlayingList) {
        Soundtrack soundtrack = thread.getSoundtrack();
        if(!(soundtrack instanceof SingleSoundtrack)) {
            return false;
        }
        SingleSoundtrack singleSoundtrack = (SingleSoundtrack) soundtrack;
        for(SingleSoundtrack keepPlaying : keepPlayingList) {
            if(singleSoundtrack.getUserID() == keepPlaying.getUserID()) {
                return true;
            }
        }
        return false;
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
            threadMap.remove(singleSoundtrack.getUserID());
        }
    }
}
