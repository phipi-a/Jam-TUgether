package de.pcps.jamtugether.model.music.soundtrack.runnable;

import androidx.annotation.NonNull;

import java.util.HashMap;

import de.pcps.jamtugether.model.instrument.base.Instrument;
import de.pcps.jamtugether.model.instrument.base.Instruments;
import de.pcps.jamtugether.model.music.sound.Sound;
import de.pcps.jamtugether.model.music.soundpool.base.BaseSoundPool;
import de.pcps.jamtugether.model.music.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.music.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.music.soundtrack.runnable.base.SoundtrackRunnable;

public class CompositeSoundtrackRunnable extends SoundtrackRunnable {

    @NonNull
    private final CompositeSoundtrack soundtrack;

    /**
     * maps soundtrack id to a sound pool
     */
    @NonNull
    private final HashMap<Integer, BaseSoundPool> soundPoolMap;

    public CompositeSoundtrackRunnable(@NonNull CompositeSoundtrack soundtrack, @NonNull Instruments instruments) {
        super(soundtrack);
        this.soundtrack = soundtrack;
        this.soundPoolMap = new HashMap<>();

        for(SingleSoundtrack singleSoundtrack : soundtrack.getSoundtracks()) {
            Instrument instrument = instruments.fromServer(singleSoundtrack.getInstrument());
            soundPoolMap.put(singleSoundtrack.getUserID(), instrument.createSoundPool());
        }
    }

    @Override
    public void play(int millis) {
        for(SingleSoundtrack singleSoundtrack : soundtrack.getSoundtracks()) {
            for(Sound sound : singleSoundtrack.getSoundsFor(millis)) {
                BaseSoundPool soundPool = soundPoolMap.get(singleSoundtrack.getUserID());
                if(soundPool == null) {
                    continue;
                }
                soundPool.onPlayElement(sound.getElement(), sound.getPitch(), sound.getLength());
            }
        }
    }

    @Override
    protected void stopSound() {
        for(BaseSoundPool soundPool : soundPoolMap.values()) {
            soundPool.stop();
        }
    }

    public void setVolume(float volume) {
        soundtrack.setVolume(volume);
        for(BaseSoundPool soundPool : soundPoolMap.values()) {
            soundPool.setVolume(volume);
        }
    }
}
