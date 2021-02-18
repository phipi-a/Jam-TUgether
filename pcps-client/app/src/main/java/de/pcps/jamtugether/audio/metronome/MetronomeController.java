package de.pcps.jamtugether.audio.metronome;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.model.Beat;

@Singleton
public class MetronomeController {

    @NonNull
    private final MetronomePlayer metronomePlayer;

    @Inject
    public MetronomeController(@NonNull MetronomePlayer metronomePlayer, @NonNull RoomRepository roomRepository, @NonNull SoundtrackRepository soundtrackRepository) {
        this.metronomePlayer = metronomePlayer;

        Observer<Beat> beatObserver = Metronome.getInstance()::updateBeat;

        roomRepository.getUserInRoom().observeForever(userInRoom -> {
            if (userInRoom) {
                soundtrackRepository.getBeat().observeForever(beatObserver);
            } else {
                soundtrackRepository.getBeat().removeObserver(beatObserver);
            }
        });
    }

    public void onMetronomeButtonClicked() {
        boolean active = !metronomePlayer.isActive();
        metronomePlayer.setActive(active);
    }

    public void onStartedRecordingSoundtrack() {
        metronomePlayer.play();
    }

    public void onFinishedRecordingSoundtrack() {
        metronomePlayer.stop();
    }
}
