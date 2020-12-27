package de.pcps.jamtugether.audio.player;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.audio.player.composite.CompositeSoundtrackPlayer;
import de.pcps.jamtugether.audio.player.single.SingleSoundtrackPlayer;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;

@Singleton
public class SoundtrackController implements Soundtrack.OnChangeCallback {

    @NonNull
    private final SingleSoundtrackPlayer singleSoundtrackPlayer;

    @NonNull
    private final CompositeSoundtrackPlayer compositeSoundtrackPlayer;

    @Inject
    public SoundtrackController(@NonNull SingleSoundtrackPlayer singleSoundtrackPlayer, @NonNull CompositeSoundtrackPlayer compositeSoundtrackPlayer) {
        this.singleSoundtrackPlayer = singleSoundtrackPlayer;
        this.compositeSoundtrackPlayer = compositeSoundtrackPlayer;
    }

    @Override
    public void onVolumeChanged(@NonNull Soundtrack soundtrack, float volume) {
        if(soundtrack instanceof SingleSoundtrack) {
            singleSoundtrackPlayer.changeVolume((SingleSoundtrack) soundtrack, volume);
        } else {
            compositeSoundtrackPlayer.changeVolume((CompositeSoundtrack) soundtrack, volume);
        }
    }

    @Override
    public void onPlayPauseButtonClicked(@NonNull Soundtrack soundtrack) {
        if(soundtrack instanceof SingleSoundtrack) {
            singleSoundtrackPlayer.playOrPause((SingleSoundtrack) soundtrack);
        } else {
            compositeSoundtrackPlayer.playOrPause((CompositeSoundtrack) soundtrack);
        }
    }

    @Override
    public void onFastForwardButtonClicked(@NonNull Soundtrack soundtrack) {
        if(soundtrack instanceof SingleSoundtrack) {
            singleSoundtrackPlayer.fastForward((SingleSoundtrack) soundtrack);
        } else {
            compositeSoundtrackPlayer.fastForward((CompositeSoundtrack) soundtrack);
        }
    }

    @Override
    public void onFastRewindButtonClicked(@NonNull Soundtrack soundtrack) {
        if(soundtrack instanceof SingleSoundtrack) {
            singleSoundtrackPlayer.fastRewind((SingleSoundtrack) soundtrack);
        } else {
            compositeSoundtrackPlayer.fastRewind((CompositeSoundtrack) soundtrack);
        }
    }

    @Override
    public void onStopButtonClicked(@NonNull Soundtrack soundtrack) {
        if(soundtrack instanceof SingleSoundtrack) {
            singleSoundtrackPlayer.stop((SingleSoundtrack) soundtrack);
        } else {
            compositeSoundtrackPlayer.stop((CompositeSoundtrack) soundtrack);
        }
    }
}
