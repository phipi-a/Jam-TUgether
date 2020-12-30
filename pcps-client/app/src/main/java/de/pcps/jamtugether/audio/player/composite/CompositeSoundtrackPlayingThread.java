package de.pcps.jamtugether.audio.player.composite;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.player.base.OnSoundtrackFinishedCallback;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;
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

    @NonNull
    @Override
    public List<SoundWithStreamID> play(int millis, boolean finishSounds) {
        List<SoundWithStreamID> soundsWithStreamIDs = new ArrayList<>();
        for (SingleSoundtrack singleSoundtrack : compositeSoundtrack.getSoundtracks()) {
            for (Sound sound : singleSoundtrack.getSoundsFor(millis, finishSounds)) {
                Instrument instrument = singleSoundtrack.getInstrument();
                BaseSoundPool soundPool = singleSoundtrack.getSoundPool();
                if(instrument == null || soundPool == null) {
                    continue;
                }
                int soundRes = instrument.getSoundResource(sound.getElement());
                int streamID = soundPool.playSoundRes(soundRes, sound.getPitch());
                soundsWithStreamIDs.add(new SoundWithStreamID(sound, streamID));
            }
        }
        return soundsWithStreamIDs;
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
            for (Sound sound : singleSoundtrack.getSoundSequence()) {
                if (sound.getEndTime() == millis && streamIDsMap.containsKey(sound)) {
                    if(singleSoundtrack.getSoundPool() != null) {
                        singleSoundtrack.getSoundPool().stopSound(streamIDsMap.get(sound));
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
