package de.pcps.jamtugether.audio.player.composite;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;
import de.pcps.jamtugether.model.sound.SoundWithStreamID;
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

    @NonNull
    @Override
    public List<SoundWithStreamID> play(int millis, boolean finishSounds) {
        List<SoundWithStreamID> soundsWithStreamIDs = new ArrayList<>();
        for (SingleSoundtrack singleSoundtrack : compositeSoundtrack.getSoundtracks()) {
            for (Sound sound : singleSoundtrack.getSoundsFor(millis, finishSounds)) {
                BaseSoundPool soundPool = singleSoundtrack.getSoundPool();
                if (soundPool == null) {
                    continue;
                }
                int soundRes = singleSoundtrack.getInstrument().getSoundResource(sound.getElement());
                int streamID = soundPool.playSoundRes(soundRes, sound.getPitch());
                soundsWithStreamIDs.add(new SoundWithStreamID(sound, streamID));
            }
        }
        return soundsWithStreamIDs;
    }

    @Override
    protected void stopAllSounds() {
        for (SingleSoundtrack singleSoundtrack : compositeSoundtrack.getSoundtracks()) {
            singleSoundtrack.getSoundPool().stopAllSounds();
        }
    }

    @Override
    public void setVolume(float volume) {
        compositeSoundtrack.postVolume(volume);
        for (SingleSoundtrack singleSoundtrack : compositeSoundtrack.getSoundtracks()) {
            singleSoundtrack.getSoundPool().setVolume(volume / 100);
        }
    }

    @Override
    protected void stopSounds(int millis) {
        for (SingleSoundtrack singleSoundtrack : compositeSoundtrack.getSoundtracks()) {
            for (Sound sound : singleSoundtrack.getSoundSequence()) {
                if (sound.getEndTime() == millis && streamIDsMap.containsKey(sound)) {
                    singleSoundtrack.getSoundPool().stopSound(streamIDsMap.get(sound));
                }
            }
        }
    }
}
