package de.pcps.jamtugether.audio.player.composite;

import androidx.annotation.NonNull;

import java.util.HashMap;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.base.Instruments;
import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.audio.player.base.SoundtrackPlayingThread;

public class CompositeSoundtrackPlayingThread extends SoundtrackPlayingThread {

    @NonNull
    private final CompositeSoundtrack soundtrack;

    /**
     * maps soundtrack id to a sound pool
     */
    @NonNull
    private final HashMap<Integer, BaseSoundPool> soundPoolMap;

    public CompositeSoundtrackPlayingThread(@NonNull CompositeSoundtrack soundtrack, @NonNull Instruments instruments) {
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
        soundtrack.postVolume(volume);
        for(BaseSoundPool soundPool : soundPoolMap.values()) {
            soundPool.setVolume(volume);
        }
    }
}
