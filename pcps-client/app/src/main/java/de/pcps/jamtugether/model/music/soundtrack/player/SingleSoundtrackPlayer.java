package de.pcps.jamtugether.model.music.soundtrack.player;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.model.instrument.base.Instrument;
import de.pcps.jamtugether.model.instrument.base.Instruments;
import de.pcps.jamtugether.model.music.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.music.soundtrack.base.Soundtrack;

@Singleton
public class SingleSoundtrackPlayer {

    @NonNull
    private final Instruments instruments;

    @Inject
    public SingleSoundtrackPlayer(@NonNull Instruments instruments) {
        this.instruments = instruments;
    }

    public void changeVolume(@NonNull SingleSoundtrack soundtrack, float volume) {
        // todo
        soundtrack.setVolume(volume);
    }

    public void playOrPause(@NonNull SingleSoundtrack soundtrack) {
        if (soundtrack.getSoundSequence().isEmpty()) {
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

    private void play(@NonNull SingleSoundtrack soundtrack) {
        String serverString = soundtrack.getSoundSequence().get(0).getInstrument();
        Instrument instrument = instruments.fromServer(serverString);

        int progress = soundtrack.getProgress().getValue();
        // todo

        soundtrack.setState(Soundtrack.State.PLAYING);
    }

    private void pause(@NonNull SingleSoundtrack soundtrack) {
        // todo
        soundtrack.setState(Soundtrack.State.PAUSED);
    }

    private void resume(@NonNull SingleSoundtrack soundtrack) {
        play(soundtrack);
    }

    public void fastForward(@NonNull SingleSoundtrack soundtrack) {
        // todo update progress
    }

    public void fastRewind(@NonNull SingleSoundtrack soundtrack) {
        // todo update progress
    }

    public void stop(@NonNull SingleSoundtrack soundtrack) {
        // todo
        soundtrack.setState(Soundtrack.State.STOPPED);
        soundtrack.setProgress(0);
    }
}
