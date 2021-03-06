package de.pcps.jamtugether.audio.player.single;

import androidx.annotation.NonNull;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.sound.BaseSoundPool;
import de.pcps.jamtugether.audio.sound.PlaySoundThread;
import de.pcps.jamtugether.model.Sound;
import de.pcps.jamtugether.audio.sound.model.SoundWithStreamID;
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
    public void play(int millis, boolean finishSounds, @NonNull PlaySoundThread.OnSoundWithIDPlayedCallback callback) {
        Instrument instrument = soundtrack.getInstrument();
        BaseSoundPool soundPool = soundtrack.getSoundPool();
        if (soundPool == null) {
            return;
        }
        for (Sound sound : soundtrack.getSoundsFor(millis, finishSounds)) {
            int soundRes = instrument.getSoundResource(sound.getPitch());
            soundPool.playSoundRes(soundRes, streamID -> callback.onSoundPlayed(new SoundWithStreamID(sound, streamID)));
        }
    }

    @Override
    public void setVolume(float volume) {
        soundtrack.setVolume(volume);
        if (soundtrack.getSoundPool() != null) {
            soundtrack.getSoundPool().setVolume(volume / 100);
        }
    }

    @Override
    protected void stopSounds(int millis) {
        Instrument instrument = soundtrack.getInstrument();
        if (!instrument.soundsNeedToBeStopped()) {
            return;
        }
        for (Sound sound : soundtrack.getSoundSequence()) {
            if (sound.getEndTime() <= millis && streamIDsMap.containsKey(sound)) {
                if (soundtrack.getSoundPool() != null) {
                    Integer streamID = streamIDsMap.get(sound);
                    if (streamID != null) {
                        soundtrack.getSoundPool().stopSound(streamID);
                    }
                }
            }
        }
    }

    @Override
    protected void stopAllSounds() {
        if (soundtrack.getSoundPool() != null) {
            soundtrack.getSoundPool().stopAllSounds();
        }
    }
}
