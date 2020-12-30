package de.pcps.jamtugether.audio.player.base;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;

import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.model.sound.SoundWithStreamID;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.utils.TimeUtils;

public abstract class SoundtrackPlayingThread extends Thread {

    private static final int FAST_FORWARD_OFFSET = (int) TimeUtils.ONE_SECOND;
    private static final int FAST_REWIND_OFFSET = (int) -TimeUtils.ONE_SECOND;

    private boolean running = false;
    private boolean paused = false;
    private boolean stopped = false;
    private boolean finished = false;

    private boolean justForwarded = false;
    private boolean justResumed = false;

    private int progressInMillis;

    /**
     * last time stamp of thread
     */
    private long lastMillis;

    /**
     * last progress in millis that was played in soundtrack
     */
    private long lastProgressInMillis = -1;

    @NonNull
    private final Soundtrack soundtrack;

    /**
     * maps a sound to the given stream id
     */
    @NonNull
    protected final HashMap<Sound, Integer> streamIDsMap;

    public SoundtrackPlayingThread(@NonNull Soundtrack soundtrack) {
        this.soundtrack = soundtrack;
        this.streamIDsMap = new HashMap<>();
    }

    @Override
    public void run() {
        while (!stopped) {
            if (paused || finished) {
                continue;
            }
            if (progressInMillis == soundtrack.getLength()) {
                soundtrack.postState(Soundtrack.State.IDLE);
                stopAllSounds();
                finished = true;
                continue;
            }
            if (progressInMillis != lastProgressInMillis) { // in order to not play a sound more than once
                lastProgressInMillis = progressInMillis;
                List<SoundWithStreamID> soundsWithStreamIDs = play(progressInMillis, justResumed || justForwarded);
                for (SoundWithStreamID soundWithStreamID : soundsWithStreamIDs) {
                    streamIDsMap.put(soundWithStreamID.getSound(), soundWithStreamID.getStreamID());
                }
                stopSounds(progressInMillis);
                if (justResumed) {
                    justResumed = false;
                }
                if (justForwarded) {
                    justForwarded = false;
                }
            }

            long millis = System.currentTimeMillis();
            this.progressInMillis += (int) (millis - lastMillis);
            soundtrack.postProgress(calculateProgress(progressInMillis));
            this.lastMillis = millis;
        }
    }

    /**
     * plays all sounds that start at the given time
     * resumes sounds if necessary
     * returns a list of sounds with their corresponding streaming ids from the sounds that are playing
     *
     * @param finishSounds indicates whether sounds have to be finished after having been interrupted by pause
     *                     or fastRewind/fastForward
     */
    @NonNull
    public abstract List<SoundWithStreamID> play(int millis, boolean finishSounds);

    public void play() {
        lastMillis = System.currentTimeMillis();
        if (!running) {
            progressInMillis = 0;
            super.start();
            running = true;
        }
        if(finished) {
            progressInMillis = 0;
            finished = false;
        }
        soundtrack.postProgress(calculateProgress(progressInMillis));
        soundtrack.postState(Soundtrack.State.PLAYING);
    }

    public void pause() {
        paused = true;
        stopAllSounds();
        soundtrack.postState(Soundtrack.State.PAUSED);
    }

    public void resumeSoundtrack() {
        lastMillis = System.currentTimeMillis();
        justResumed = true;
        paused = false;
        soundtrack.postState(Soundtrack.State.PLAYING);
    }

    public void fastForward() {
        stopAllSounds();
        justForwarded = true;
        int progressInMillis = this.progressInMillis + FAST_FORWARD_OFFSET;
        this.progressInMillis = Math.min(progressInMillis, soundtrack.getLength());
        soundtrack.postProgress(calculateProgress(this.progressInMillis));
    }

    public void fastRewind() {
        stopAllSounds();
        justForwarded = true;
        int progressInMillis = this.progressInMillis + FAST_REWIND_OFFSET;
        this.progressInMillis = Math.max(progressInMillis, 0);
        soundtrack.postProgress(calculateProgress(this.progressInMillis));
        if (finished) { // in order to not start playing immediately
            pause();
            finished = false;
        }
    }

    public void stopSoundtrack() {
        stopAllSounds();
        stopped = true;
        soundtrack.postProgress(0);
        soundtrack.postState(Soundtrack.State.STOPPED);
    }

    public void stopThread() {
        stopAllSounds();
        stopped = true;
    }

    private int calculateProgress(int millis) {
        return (int) ((millis / (float) soundtrack.getLength()) * 100);
    }

    protected abstract void setVolume(float volume);

    protected abstract void stopSounds(int millis);

    protected abstract void stopAllSounds();
}
