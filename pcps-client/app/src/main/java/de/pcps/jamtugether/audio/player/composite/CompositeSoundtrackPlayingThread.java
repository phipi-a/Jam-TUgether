package de.pcps.jamtugether.audio.player.composite;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.player.base.OnSoundtrackFinishedCallback;
import de.pcps.jamtugether.audio.sound.OnSoundWithIDPlayedCallback;
import de.pcps.jamtugether.audio.sound.pool.base.BaseSoundPool;
import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.model.sound.SoundWithStreamID;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.audio.player.base.SoundtrackPlayingThread;

public class CompositeSoundtrackPlayingThread extends SoundtrackPlayingThread {

    @NonNull
    private final CompositeSoundtrack compositeSoundtrack;

    public CompositeSoundtrackPlayingThread(@NonNull CompositeSoundtrack soundtrack, @NonNull OnSoundtrackFinishedCallback callback) {
        super(soundtrack, callback);
        this.compositeSoundtrack = soundtrack;
    }

    @Override
    public void play(int millis, boolean finishSounds, @NonNull OnSoundWithIDPlayedCallback callback) {
        for (SingleSoundtrack singleSoundtrack : compositeSoundtrack.getSoundtracks()) {
            for (Sound sound : singleSoundtrack.getSoundsFor(millis, finishSounds)) {
                Instrument instrument = singleSoundtrack.getInstrument();
                BaseSoundPool soundPool = singleSoundtrack.getSoundPool();
                if(instrument == null || soundPool == null) {
                    continue;
                }
                int soundRes = instrument.getSoundResource(sound.getElement());
                soundPool.playSoundRes(soundRes, sound.getPitch(), streamID -> {
                    callback.onSoundPlayed(new SoundWithStreamID(sound, streamID));
                });
            }
        }
    }

    @Override
    public void setVolume(float volume) {
        compositeSoundtrack.setVolume(volume);
        for (SingleSoundtrack singleSoundtrack : compositeSoundtrack.getSoundtracks()) {
            if(singleSoundtrack.getSoundPool() != null) {
                singleSoundtrack.getSoundPool().setVolume(volume / 100);
            }
        }
    }

    @Override
    protected void stopSounds(int millis) {
        for (SingleSoundtrack singleSoundtrack : compositeSoundtrack.getSoundtracks()) {
            Instrument instrument = singleSoundtrack.getInstrument();
            if(instrument == null || !instrument.soundsNeedToBeStopped()) {
                continue;
            }
            for (Sound sound : singleSoundtrack.getSoundSequence()) {
                if (sound.getEndTime() <= millis && streamIDsMap.containsKey(sound)) {
                    if(singleSoundtrack.getSoundPool() != null) {
                        Integer streamID = streamIDsMap.get(sound);
                        if(streamID != null) {
                            singleSoundtrack.getSoundPool().stopSound(streamID);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void stopAllSounds() {
        for (SingleSoundtrack singleSoundtrack : compositeSoundtrack.getSoundtracks()) {
            if(singleSoundtrack.getSoundPool() != null) {
                singleSoundtrack.getSoundPool().stopAllSounds();
            }
        }
    }
}
