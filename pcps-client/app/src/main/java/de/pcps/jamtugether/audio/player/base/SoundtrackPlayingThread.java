package de.pcps.jamtugether.audio.player.base;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.model.sound.SoundWithStreamID;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.utils.TimeUtils;

public abstract class SoundtrackPlayingThread extends Thread {

    private static final int FAST_FORWARD_OFFSET = (int) TimeUtils.ONE_SECOND;
    private static final int FAST_REWIND_OFFSET = (int) -TimeUtils.ONE_SECOND;

    private boolean running = false;
    private boolean stopped = false;

    private boolean justForwarded = false;
    private boolean justResumed = false;

    private int progressInMillis;

    /**
     * time stamp of last iteration in this thread
     */
    private long lastMillis;

    /**
     * progress in millis that were played last in soundtrack
     */
    private long lastProgressInMillis = -1;

    @NonNull
    private final Soundtrack soundtrack;

    @NonNull
    private final OnSoundtrackFinishedCallback callback;

    /**
     * maps a sound to the given stream id
     */
    @NonNull
    protected final HashMap<Sound, Integer> streamIDsMap;

    public SoundtrackPlayingThread(@NonNull Soundtrack soundtrack, @NonNull OnSoundtrackFinishedCallback callback) {
        this.soundtrack = soundtrack;
        this.progressInMillis = soundtrack.getProgressInMillis();
        this.callback = callback;
        this.streamIDsMap = new HashMap<>();
    }

    @Override
    public void run() {
        while (!stopped) {
            if (soundtrackIsFinished()) {
                callback.onSoundtrackFinished(this);
                // even though thread is technically being stopped by callback
                // break is needed in order to stop immediately to avoid executing further code
                break;
            }

            if (progressInMillis != lastProgressInMillis) { // in order to not play a sound more than once
                lastProgressInMillis = progressInMillis;

                stopSounds(progressInMillis);

                List<SoundWithStreamID> soundsWithStreamIDs = play(progressInMillis, justResumed || justForwarded);

                for (SoundWithStreamID soundWithStreamID : soundsWithStreamIDs) {
                    streamIDsMap.put(soundWithStreamID.getSound(), soundWithStreamID.getStreamID());
                }

                if (justResumed) {
                    justResumed = false;
                }
                if (justForwarded) {
                    justForwarded = false;
                }
            }

            long millis = System.currentTimeMillis();
            this.progressInMillis += (int) (millis - lastMillis);
            soundtrack.postProgressInMillis(progressInMillis);
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
            if(soundtrackIsFinished()) {
                setProgressInMillis(0);
            }
            super.start();
            running = true;
        }
        soundtrack.postState(Soundtrack.State.PLAYING);
    }

    public void pause() {
        stopped = true;
        stopAllSounds();
        soundtrack.postState(Soundtrack.State.PAUSED);
    }

    public void resumeSoundtrack() {
        justResumed = true;
        play();
    }

    public void fastForward() {
        stopAllSounds();
        justForwarded = true;
        setProgressInMillis(Math.min(progressInMillis + FAST_FORWARD_OFFSET, soundtrack.getLength()));
    }

    public void fastRewind() {
        stopAllSounds();
        justForwarded = true;
        setProgressInMillis(Math.max(progressInMillis + FAST_REWIND_OFFSET, 0));
    }

    public void stopSoundtrack() {
        stopped = true;
        stopAllSounds();
        soundtrack.postState(Soundtrack.State.STOPPED);
    }

    private boolean soundtrackIsFinished() {
        return progressInMillis >= soundtrack.getLength();
    }

    private void setProgressInMillis(int progressInMillis) {
        this.progressInMillis = progressInMillis;
        soundtrack.postProgressInMillis(progressInMillis);
    }

    public boolean isPlaying() {
        return soundtrack.getState().getValue() == Soundtrack.State.PLAYING;
    }

    protected abstract void setVolume(float volume);

    /**
     * stop sounds that end at given timestamp or before
     */
    protected abstract void stopSounds(int millis);

    protected abstract void stopAllSounds();

    @NonNull
    public Soundtrack getSoundtrack() {
        return soundtrack;
    }

}
