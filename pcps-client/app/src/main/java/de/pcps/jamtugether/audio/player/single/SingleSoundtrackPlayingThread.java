package de.pcps.jamtugether.audio.player.single;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.model.sound.SoundWithStreamID;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.audio.player.base.SoundtrackPlayingThread;
import timber.log.Timber;

public class SingleSoundtrackPlayingThread extends SoundtrackPlayingThread {

    @NonNull
    private final SingleSoundtrack soundtrack;

    public SingleSoundtrackPlayingThread(@NonNull SingleSoundtrack soundtrack) {
        super(soundtrack);
        this.soundtrack = soundtrack;
    }

    @NonNull
    @Override
    public List<SoundWithStreamID> play(int millis, boolean finishSounds) {
        List<SoundWithStreamID> soundsWithStreamIDs = new ArrayList<>();
        for (Sound sound : soundtrack.getSoundsFor(millis, finishSounds)) {
            int soundRes = soundtrack.getInstrument().getSoundResource(sound.getElement());
            int streamID = soundtrack.getSoundPool().playSoundRes(soundRes, sound.getPitch());
            soundsWithStreamIDs.add(new SoundWithStreamID(sound, streamID));
        }
        return soundsWithStreamIDs;
    }

    @Override
    public void setVolume(float volume) {
        soundtrack.setVolume(volume);
        soundtrack.getSoundPool().setVolume(volume / 100);
    }

    @Override
    protected void stopSounds(int millis) {
        for (Sound sound : soundtrack.getSoundSequence()) {
            if (sound.getEndTime() == millis && streamIDsMap.containsKey(sound)) {
                soundtrack.getSoundPool().stopSound(streamIDsMap.get(sound));
            }
        }
    }

    @Override
    protected void stopAllSounds() {
        soundtrack.getSoundPool().stopAllSounds();
    }
}
