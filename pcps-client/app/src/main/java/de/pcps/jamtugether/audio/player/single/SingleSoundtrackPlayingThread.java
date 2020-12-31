package de.pcps.jamtugether.audio.player.single;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.player.base.OnSoundtrackFinishedCallback;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;
import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.model.sound.SoundWithStreamID;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.audio.player.base.SoundtrackPlayingThread;
import timber.log.Timber;

public class SingleSoundtrackPlayingThread extends SoundtrackPlayingThread {

    @NonNull
    private final SingleSoundtrack soundtrack;

    public SingleSoundtrackPlayingThread(@NonNull SingleSoundtrack soundtrack, @NonNull OnSoundtrackFinishedCallback callback) {
        super(soundtrack, callback);
        this.soundtrack = soundtrack;
    }

    @NonNull
    @Override
    public List<SoundWithStreamID> play(int millis, boolean finishSounds) {
        List<SoundWithStreamID> soundsWithStreamIDs = new ArrayList<>();
        for (Sound sound : soundtrack.getSoundsFor(millis, finishSounds)) {
            Instrument instrument = soundtrack.getInstrument();
            BaseSoundPool soundPool = soundtrack.getSoundPool();
            if(instrument == null || soundPool == null) {
                continue;
            }
            int soundRes = instrument.getSoundResource(sound.getElement());
            int streamID = soundPool.playSoundRes(soundRes, sound.getPitch());
            soundsWithStreamIDs.add(new SoundWithStreamID(sound, streamID));
        }
        return soundsWithStreamIDs;
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
        for (Sound sound : soundtrack.getSoundSequence()) {
            if (sound.getEndTime() <= millis && streamIDsMap.containsKey(sound)) {
                if(soundtrack.getSoundPool() != null) {
                    soundtrack.getSoundPool().stopSound(streamIDsMap.get(sound));
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
