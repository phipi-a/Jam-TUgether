package de.pcps.jamtugether.audio.metronome;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.model.beat.Beat;

@Singleton
public class MetronomeController {

    @NonNull
    private static final Metronome metronome = Metronome.getInstance();

    @NonNull
    private final MetronomePlayer metronomePlayer;

    @Inject
    public MetronomeController(@NonNull MetronomePlayer metronomePlayer, @NonNull RoomRepository roomRepository, @NonNull SoundtrackRepository soundtrackRepository) {
        this.metronomePlayer = metronomePlayer;

        Observer<Beat> beatObserver = metronomePlayer::onBeatChanged;

        roomRepository.getUserInRoom().observeForever(userInRoom -> {
            if(userInRoom) {
                soundtrackRepository.getBeat().observeForever(beatObserver);
            } else {
                soundtrackRepository.getBeat().removeObserver(beatObserver);
            }
        });
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
}
