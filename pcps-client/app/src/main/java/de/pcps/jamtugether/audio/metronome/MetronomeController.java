package de.pcps.jamtugether.audio.metronome;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.model.beat.Beat;

@Singleton
public class MetronomeController implements Metronome.OnChangeCallback {

    @NonNull
    private static final Metronome metronome = Metronome.getInstance();

    @NonNull
    private final MetronomePlayer metronomePlayer;

    @Inject
    public MetronomeController(@NonNull MetronomePlayer metronomePlayer) {
        this.metronomePlayer = metronomePlayer;
    }

    public void onPlayStopButtonClicked() {
        Boolean playing = metronome.getPlaying().getValue();
        if(playing == null) {
            return;
        }
        if(playing) {
            metronomePlayer.play();
        } else {
            metronomePlayer.stop();
        }
    }

    @Override
    public void onBeatChanged(@NonNull Beat beat) {
        metronomePlayer.onBeatChanged(beat);
    }
}
