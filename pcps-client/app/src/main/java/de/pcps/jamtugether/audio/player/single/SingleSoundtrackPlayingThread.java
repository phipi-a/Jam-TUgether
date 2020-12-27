package de.pcps.jamtugether.audio.player.single;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.audio.soundpool.base.BaseSoundPool;
import de.pcps.jamtugether.model.sound.SoundWithStreamID;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.audio.player.base.SoundtrackPlayingThread;

public class SingleSoundtrackPlayingThread extends SoundtrackPlayingThread {

    @NonNull
    private final SingleSoundtrack soundtrack;

    @NonNull
    private final BaseSoundPool soundPool;

    public SingleSoundtrackPlayingThread(@NonNull SingleSoundtrack soundtrack) {
        super(soundtrack);
        this.soundtrack = soundtrack;

        soundPool = soundtrack.getSoundPool();
    }

    @Override
    public List<SoundWithStreamID> play(int millis) {
        List<SoundWithStreamID> soundsWithStreamIDs = new ArrayList<>();
        for(Sound sound : soundtrack.getSoundsFor(millis)) {
            int streamID = soundPool.onPlayElement(sound.getElement(), sound.getPitch());
            soundsWithStreamIDs.add(new SoundWithStreamID(sound, streamID));
        }
        return soundsWithStreamIDs;
    }

    @Override
    protected void stopAllSounds() {
        soundPool.stopAllSounds();
    }

    @Override
    public void setVolume(float volume) {
        soundtrack.postVolume(volume);
        soundPool.setVolume(volume / 100);
    }

    @Override
    protected void stopSounds(int millis) {
        for(Sound sound : soundtrack.getSoundSequence()) {
            if(sound.getEndTime() == millis && streamIDsMap.containsKey(sound)) {
                soundPool.stopSound(streamIDsMap.get(sound));
            }
        }
    }
}
