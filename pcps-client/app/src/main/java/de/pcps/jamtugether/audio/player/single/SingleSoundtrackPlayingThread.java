package de.pcps.jamtugether.audio.player.single;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.player.base.OnSoundtrackFinishedCallback;
import de.pcps.jamtugether.audio.sound.OnSoundWithIDPlayedCallback;
import de.pcps.jamtugether.audio.sound.pool.base.BaseSoundPool;
import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.model.sound.SoundWithStreamID;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.audio.player.base.SoundtrackPlayingThread;

public class SingleSoundtrackPlayingThread extends SoundtrackPlayingThread {

    @NonNull
    private final SingleSoundtrack soundtrack;

    public SingleSoundtrackPlayingThread(@NonNull SingleSoundtrack soundtrack, @NonNull OnSoundtrackFinishedCallback callback) {
        super(soundtrack, callback);
        this.soundtrack = soundtrack;
    }

    @Override
    public void play(int millis, boolean finishSounds, @NonNull OnSoundWithIDPlayedCallback callback) {
        for (Sound sound : soundtrack.getSoundsFor(millis, finishSounds)) {
            Instrument instrument = soundtrack.getInstrument();
            BaseSoundPool soundPool = soundtrack.getSoundPool();
            if(instrument == null || soundPool == null) {
                continue;
            }
            int soundRes = instrument.getSoundResource(sound.getElement());
            soundPool.playSoundRes(soundRes, sound.getPitch(), streamID -> {
                callback.onSoundPlayed(new SoundWithStreamID(sound, streamID));
            });
        }
    }

    @Override
    public void setVolume(float volume) {
        soundtrack.setVolume(volume);
        if(soundtrack.getSoundPool() != null) {
            soundtrack.getSoundPool().setVolume(volume / 100);
        }
    }

    @Override
    protected void stopSounds(int millis) {
        Instrument instrument = soundtrack.getInstrument();
        if(instrument == null || !instrument.soundsNeedToBeStopped()) {
            return;
        }
        for (Sound sound : soundtrack.getSoundSequence()) {
            if (sound.getEndTime() <= millis && streamIDsMap.containsKey(sound)) {
                if(soundtrack.getSoundPool() != null) {
                    Integer streamID = streamIDsMap.get(sound);
                    if(streamID != null) {
                        soundtrack.getSoundPool().stopSound(streamID);
                    }
                }
            }
        }
    }

    @Override
    protected void stopAllSounds() {
        if(soundtrack.getSoundPool() != null) {
            soundtrack.getSoundPool().stopAllSounds();
        }
    }
}
