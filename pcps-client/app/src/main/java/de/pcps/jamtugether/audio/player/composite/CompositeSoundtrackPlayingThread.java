package de.pcps.jamtugether.audio.player.composite;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.audio.player.base.SoundtrackPlayingThread;

public class CompositeSoundtrackPlayingThread extends SoundtrackPlayingThread {

    @NonNull
    private final CompositeSoundtrack compositeSoundtrack;

    public CompositeSoundtrackPlayingThread(@NonNull CompositeSoundtrack soundtrack) {
        super(soundtrack);
        this.compositeSoundtrack = soundtrack;
    }

    @Override
    public void play(int millis) {
        for(SingleSoundtrack singleSoundtrack : compositeSoundtrack.getSoundtracks()) {
            for(Sound sound : singleSoundtrack.getSoundsFor(millis)) {
                BaseSoundPool soundPool = singleSoundtrack.getSoundPool();
                if(soundPool == null) {
                    continue;
                }
                soundPool.onPlayElement(sound.getElement(), sound.getPitch(), sound.getLength());
            }
        }
    }

    @Override
    protected void stopSound() {
        for(SingleSoundtrack singleSoundtrack : compositeSoundtrack.getSoundtracks()) {
            singleSoundtrack.getSoundPool().stop();
        }
    }

    @Override
    public void setVolume(float volume) {
        compositeSoundtrack.postVolume(volume);
        for(SingleSoundtrack singleSoundtrack : compositeSoundtrack.getSoundtracks()) {
            singleSoundtrack.getSoundPool().setVolume(volume / 100);
        }
    }
}
