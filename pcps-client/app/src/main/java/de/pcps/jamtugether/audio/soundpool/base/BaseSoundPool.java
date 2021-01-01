package de.pcps.jamtugether.audio.soundpool.base;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.base.Instruments;

/**
 * A simple sound pool wrapper
 */
public abstract class BaseSoundPool {

    @NonNull
    protected final SoundPool soundPool;

    @NonNull
    private final List<Integer> loadedSoundIDs;

    @NonNull
    private final List<Integer> streamIDs;

    /**
     * maps sound resource id to sound id
     */
    @NonNull
    private final HashMap<Integer, Integer> soundResMap;

    protected float volume = 1;

    public BaseSoundPool(@NonNull Context context, int maxStreams, @NonNull Integer... soundResIDs) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(maxStreams)
                .setAudioAttributes(audioAttributes)
                .build();

        this.loadedSoundIDs = new ArrayList<>();
        this.streamIDs = Collections.synchronizedList(new ArrayList<>());
        this.soundResMap = new HashMap<>();

        for (Integer soundResID : soundResIDs) {
            int soundID = soundPool.load(context, soundResID, 1);
            soundResMap.put(soundResID, soundID);
        }

        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> loadedSoundIDs.add(sampleId));
    }

    protected abstract int play(int soundID, float pitch);

    public int playSoundRes(int soundResID, float pitch) {
        Integer soundID = soundResMap.get(soundResID);
        if (soundID == null) {
            return 0;
        }
        if (soundIsLoaded(soundID)) {
            int streamID = play(soundID, pitch);
            if (!streamIDs.contains(streamID) && streamID != 0) {
                streamIDs.add(streamID);
            }
            return streamID;
        }
        return 0;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void stopSound(int streamID) {
        if (streamIDs.contains(streamID)) {
            soundPool.stop(streamID);
        }
    }

    public void stopAllSounds() {
        synchronized(streamIDs) {
            for (int streamID : streamIDs) {
                soundPool.stop(streamID);
            }
        }
    }

    public boolean soundIsLoaded(int soundID) {
        return loadedSoundIDs.contains(soundID);
    }

    public abstract float calculatePitch(int pitchPercentage);

    @NonNull
    public static BaseSoundPool from(@NonNull String instrumentString, @NonNull Context context) {
        Instrument instrument = Instruments.fromServer(instrumentString);
        return instrument.createSoundPool(context);
    }
}
