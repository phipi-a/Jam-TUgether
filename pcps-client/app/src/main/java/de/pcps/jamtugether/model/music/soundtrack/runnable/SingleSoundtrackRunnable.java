package de.pcps.jamtugether.model.music.soundtrack.runnable;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.model.instrument.base.Instrument;
import de.pcps.jamtugether.model.instrument.base.Instruments;
import de.pcps.jamtugether.model.music.sound.Sound;
import de.pcps.jamtugether.model.music.soundpool.base.BaseSoundPool;
import de.pcps.jamtugether.model.music.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.music.soundtrack.runnable.base.SoundtrackRunnable;

public class SingleSoundtrackRunnable extends SoundtrackRunnable {

    @NonNull
    private final SingleSoundtrack soundtrack;

    @NonNull
    private final BaseSoundPool soundPool;

    public SingleSoundtrackRunnable(@NonNull SingleSoundtrack soundtrack, @NonNull Instruments instruments) {
        super(soundtrack);
        this.soundtrack = soundtrack;

        Instrument instrument = instruments.fromServer(soundtrack.getInstrument());
        soundPool = instrument.createSoundPool();
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

    public void setVolume(float volume) {
        soundtrack.setVolume(volume);
        soundPool.setVolume(volume);
    }
}
