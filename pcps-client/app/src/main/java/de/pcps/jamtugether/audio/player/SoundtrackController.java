package de.pcps.jamtugether.audio.player;

import androidx.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.api.repositories.SoundtrackRepository;
import de.pcps.jamtugether.audio.player.composite.CompositeSoundtrackPlayer;
import de.pcps.jamtugether.audio.player.single.SingleSoundtrackPlayer;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.storage.db.SoundtrackVolumesDatabase;

@Singleton
public class SoundtrackController implements Soundtrack.OnChangeCallback {

    @NonNull
    private final SingleSoundtrackPlayer singleSoundtrackPlayer;

    @NonNull
    private final CompositeSoundtrackPlayer compositeSoundtrackPlayer;

    @NonNull
    private final SoundtrackVolumesDatabase soundtrackVolumesDatabase;

    @NonNull
    private final SoundtrackRepository soundtrackRepository;

    @Inject
    public SoundtrackController(@NonNull SingleSoundtrackPlayer singleSoundtrackPlayer, @NonNull CompositeSoundtrackPlayer compositeSoundtrackPlayer, @NonNull SoundtrackVolumesDatabase soundtrackVolumesDatabase, @NonNull RoomRepository roomRepository, @NonNull SoundtrackRepository soundtrackRepository) {
        this.singleSoundtrackPlayer = singleSoundtrackPlayer;
        this.compositeSoundtrackPlayer = compositeSoundtrackPlayer;
        this.soundtrackVolumesDatabase = soundtrackVolumesDatabase;
        this.soundtrackRepository = soundtrackRepository;

        roomRepository.getUserInRoom().observeForever(userInRoom -> {
            if (!userInRoom) {
                singleSoundtrackPlayer.stop();
                compositeSoundtrackPlayer.stop();
            }
        });
    }

    @Override
    public void onVolumeChanged(@NonNull Soundtrack soundtrack, float volume) {
        if (soundtrack.isEmpty()) {
            return;
        }
        if (soundtrack instanceof SingleSoundtrack) {
            SingleSoundtrack singleSoundtrack = (SingleSoundtrack) soundtrack;
            singleSoundtrackPlayer.changeVolume(soundtrack, volume);
            List<SingleSoundtrack> allSoundtracks = soundtrackRepository.getAllSoundtracks().getValue();
            if (allSoundtracks != null) {
                for (SingleSoundtrack s : allSoundtracks) {
                    if (s.getID().equals(singleSoundtrack.getID())) {
                        s.setVolume(volume);
                    }
                }
            }
            soundtrackVolumesDatabase.onVolumeChanged(singleSoundtrack, volume);
        } else {
            compositeSoundtrackPlayer.changeVolume(soundtrack, volume);
            CompositeSoundtrack compositeSoundtrack = soundtrackRepository.getCompositeSoundtrack().getValue();
            if (compositeSoundtrack != null) {
                compositeSoundtrack.setVolume(volume);
            }
            soundtrackVolumesDatabase.onCompositeSoundtrackVolumeChanged(volume);
        }
    }

    @Override
    public void onPlayPauseButtonClicked(@NonNull Soundtrack soundtrack) {
        if (soundtrack instanceof SingleSoundtrack) {
            singleSoundtrackPlayer.playOrPause(soundtrack);
        } else {
            compositeSoundtrackPlayer.playOrPause(soundtrack);
        }
    }

    @Override
    public void onFastForwardButtonClicked(@NonNull Soundtrack soundtrack) {
        if (soundtrack instanceof SingleSoundtrack) {
            singleSoundtrackPlayer.fastForward(soundtrack);
        } else {
            compositeSoundtrackPlayer.fastForward(soundtrack);
        }
    }

    @Override
    public void onFastRewindButtonClicked(@NonNull Soundtrack soundtrack) {
        if (soundtrack instanceof SingleSoundtrack) {
            singleSoundtrackPlayer.fastRewind(soundtrack);
        } else {
            compositeSoundtrackPlayer.fastRewind(soundtrack);
        }
    }

    @Override
    public void onStopButtonClicked(@NonNull Soundtrack soundtrack) {
        if (soundtrack instanceof SingleSoundtrack) {
            singleSoundtrackPlayer.stop(soundtrack);
        } else {
            compositeSoundtrackPlayer.stop(soundtrack);
        }
    }
}
