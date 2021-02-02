package de.pcps.jamtugether.audio.player;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.repositories.RoomRepository;
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

    @Inject
    public SoundtrackController(@NonNull SingleSoundtrackPlayer singleSoundtrackPlayer, @NonNull CompositeSoundtrackPlayer compositeSoundtrackPlayer, @NonNull SoundtrackVolumesDatabase soundtrackVolumesDatabase, @NonNull RoomRepository roomRepository) {
        this.singleSoundtrackPlayer = singleSoundtrackPlayer;
        this.compositeSoundtrackPlayer = compositeSoundtrackPlayer;
        this.soundtrackVolumesDatabase = soundtrackVolumesDatabase;

        roomRepository.getUserInRoom().observeForever(userInRoom -> {
            if (!userInRoom) {
                singleSoundtrackPlayer.stop();
                compositeSoundtrackPlayer.stop();
            }
        });
    }

    @Override
    public void onVolumeChanged(@NonNull Soundtrack soundtrack, float volume) {
        if (soundtrack instanceof SingleSoundtrack) {
            SingleSoundtrack singleSoundtrack = (SingleSoundtrack) soundtrack;
            singleSoundtrackPlayer.changeVolume(soundtrack, volume);
            soundtrackVolumesDatabase.onVolumeChanged(singleSoundtrack, volume);
        } else {
            // todo volumes data base
            compositeSoundtrackPlayer.changeVolume((CompositeSoundtrack) soundtrack, volume);
        }
    }

    @Override
    public void onPlayPauseButtonClicked(@NonNull Soundtrack soundtrack) {
        if (soundtrack instanceof SingleSoundtrack) {
            singleSoundtrackPlayer.playOrPause((SingleSoundtrack) soundtrack);
        } else {
            compositeSoundtrackPlayer.playOrPause((CompositeSoundtrack) soundtrack);
        }
    }

    @Override
    public void onFastForwardButtonClicked(@NonNull Soundtrack soundtrack) {
        if (soundtrack instanceof SingleSoundtrack) {
            singleSoundtrackPlayer.fastForward((SingleSoundtrack) soundtrack);
        } else {
            compositeSoundtrackPlayer.fastForward((CompositeSoundtrack) soundtrack);
        }
    }

    @Override
    public void onFastRewindButtonClicked(@NonNull Soundtrack soundtrack) {
        if (soundtrack instanceof SingleSoundtrack) {
            singleSoundtrackPlayer.fastRewind((SingleSoundtrack) soundtrack);
        } else {
            compositeSoundtrackPlayer.fastRewind((CompositeSoundtrack) soundtrack);
        }
    }

    @Override
    public void onStopButtonClicked(@NonNull Soundtrack soundtrack) {
        if (soundtrack instanceof SingleSoundtrack) {
            singleSoundtrackPlayer.stop((SingleSoundtrack) soundtrack);
        } else {
            compositeSoundtrackPlayer.stop((CompositeSoundtrack) soundtrack);
        }
    }
}
