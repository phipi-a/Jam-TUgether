package de.pcps.jamtugether.audio.player.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;

public abstract class SoundtrackPlayer implements SoundtrackPlayingThread.OnSoundtrackFinishedCallback {

    public void playOrPause(@NonNull Soundtrack soundtrack) {
        if (soundtrack.isEmpty()) {
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

    public void play(@NonNull Soundtrack soundtrack) {
        play(soundtrack, false);
    }

    public void play(@NonNull Soundtrack soundtrack, boolean justResumed) {
        SoundtrackPlayingThread thread = getThread(soundtrack);
        if (thread != null) {
            thread.play(justResumed);
        }
    }

    protected void pause(@NonNull Soundtrack soundtrack) {
        SoundtrackPlayingThread thread = getThread(soundtrack);
        if (thread != null) {
            thread.pause();
        }
    }

    private void resume(@NonNull Soundtrack soundtrack) {
        SoundtrackPlayingThread thread = getThread(soundtrack);
        if (thread != null) {
            thread.resumeSoundtrack();
        }
    }

    public void fastForward(@NonNull Soundtrack soundtrack) {
        SoundtrackPlayingThread thread = getThread(soundtrack);
        if (thread != null) {
            thread.fastForward();
        }
    }

    public void fastRewind(@NonNull Soundtrack soundtrack) {
        SoundtrackPlayingThread thread = getThread(soundtrack);
        if (thread != null) {
            thread.fastRewind();
        }
    }

    public void stop(@NonNull Soundtrack soundtrack) {
        SoundtrackPlayingThread thread = getThread(soundtrack);
        if (thread != null) {
            thread.stopSoundtrack();
            soundtrack.postProgressInMillis(0);
        }
    }

    public void changeVolume(@NonNull Soundtrack soundtrack, float volume) {
        SoundtrackPlayingThread thread = getThread(soundtrack);
        if (thread != null) {
            thread.setVolume(volume);
        }
    }

    public abstract boolean isPlaying();

    public abstract void stop();

    @Nullable
    protected abstract SoundtrackPlayingThread createThread(@NonNull Soundtrack soundtrack);

    @Nullable
    protected abstract SoundtrackPlayingThread getThread(@NonNull Soundtrack soundtrack);
}
