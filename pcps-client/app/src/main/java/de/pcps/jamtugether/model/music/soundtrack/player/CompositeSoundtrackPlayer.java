package de.pcps.jamtugether.model.music.soundtrack.player;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.model.instrument.base.Instrument;
import de.pcps.jamtugether.model.instrument.base.Instruments;
import de.pcps.jamtugether.model.music.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.music.soundtrack.base.Soundtrack;

// todo maybe create abstract class if worth it
@Singleton
public class CompositeSoundtrackPlayer {

    @NonNull
    private final SingleSoundtrackPlayer singleSoundtrackPlayer;

    @NonNull
    private final Instruments instruments;

    @Inject
    public CompositeSoundtrackPlayer(@NonNull SingleSoundtrackPlayer singleSoundtrackPlayer, @NonNull Instruments instruments) {
        this.singleSoundtrackPlayer = singleSoundtrackPlayer;
        this.instruments = instruments;
    }

    public void changeVolume(@NonNull CompositeSoundtrack soundtrack, float volume) {
        // todo
        soundtrack.setVolume(volume);
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
        int progress = soundtrack.getProgress().getValue();
        // todo

        soundtrack.setState(Soundtrack.State.PLAYING);
    }

    private void pause(@NonNull CompositeSoundtrack soundtrack) {
        // todo
        soundtrack.setState(Soundtrack.State.PAUSED);
    }

    private void resume(@NonNull CompositeSoundtrack soundtrack) {
        play(soundtrack);
    }

    public void fastForward(@NonNull CompositeSoundtrack soundtrack) {
        // todo update progress
    }

    public void fastRewind(@NonNull CompositeSoundtrack soundtrack) {
        // todo update progress
    }

    public void stop(@NonNull CompositeSoundtrack soundtrack) {
        // todo
        soundtrack.setState(Soundtrack.State.STOPPED);
        soundtrack.setProgress(0);
    }
}
