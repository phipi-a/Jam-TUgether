package de.pcps.jamtugether.audio.player.single;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.base.Instruments;
import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.audio.player.base.SoundtrackPlayingThread;
import timber.log.Timber;

public class SingleSoundtrackPlayingThread extends SoundtrackPlayingThread {

    @NonNull
    private final SingleSoundtrack soundtrack;

    @NonNull
    private final BaseSoundPool soundPool;

    public SingleSoundtrackPlayingThread(@NonNull SingleSoundtrack soundtrack) {
        super(soundtrack);
        this.soundtrack = soundtrack;

        soundPool = soundtrack.getSoundPool();
    }

    @Override
    public void play(int millis) {
        for(Sound sound : soundtrack.getSoundsFor(millis)) {
            soundPool.onPlayElement(sound.getElement(), sound.getPitch(), sound.getLength());
        }
    }

    @Override
    protected void stopSound() {
        soundPool.stop();
    }

    @Override
    public void setVolume(float volume) {
        soundtrack.postVolume(volume);
        soundPool.setVolume(volume);
    }
}
