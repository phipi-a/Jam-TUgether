package de.pcps.jamtugether.audio.player.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import timber.log.Timber;

public abstract class SoundtrackPlayer {

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

    private void play(@NonNull Soundtrack soundtrack) {
        SoundtrackPlayingThread thread = getThread(soundtrack);
        if (thread != null) {
            thread.play();
        }
    }

    private void pause(@NonNull Soundtrack soundtrack) {
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
        }
    }

    public void changeVolume(@NonNull Soundtrack soundtrack, float volume) {
        SoundtrackPlayingThread thread = getThread(soundtrack);
        if (thread != null) {
            thread.setVolume(volume);
        }
    }

    public abstract void stop();

    @Nullable
    protected abstract SoundtrackPlayingThread createThread(@NonNull Soundtrack soundtrack);

    @Nullable
    protected abstract SoundtrackPlayingThread getThread(@NonNull Soundtrack soundtrack);
}
