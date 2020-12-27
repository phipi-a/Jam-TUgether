package de.pcps.jamtugether.audio.soundpool.base;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.pcps.jamtugether.audio.instrument.Drums;
import de.pcps.jamtugether.audio.instrument.Shaker;
import de.pcps.jamtugether.audio.instrument.SoundResource;
import de.pcps.jamtugether.audio.soundpool.DrumsSoundPool;
import de.pcps.jamtugether.audio.soundpool.FluteSoundPool;
import de.pcps.jamtugether.audio.soundpool.ShakerSoundPool;

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
        this.streamIDs = new ArrayList<>();
        this.soundResMap = new HashMap<>();

        for (Integer soundResID : soundResIDs) {
            int soundID = soundPool.load(context, soundResID, 1);
            soundResMap.put(soundResID, soundID);
        }

        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> loadedSoundIDs.add(sampleId));
    }

    public int onPlaySoundRes(int soundResID, float pitch, int length) {
        Integer soundID = soundResMap.get(soundResID);
        if (soundID == null) {
            return 0;
        }
        if (soundIsLoaded(soundID)) {
            int streamID = play(soundID, pitch, -1);
            if (!streamIDs.contains(streamID) && streamID != 0) {
                streamIDs.add(streamID);
            }
            return streamID;
        }
        return 0;
    }

    public int onPlayElement(int element, float pitch, int length) {
        SoundResource soundResource = SoundResource.from(element);
        if (soundResource == null) {
            return 0;
        } else {
            return onPlaySoundRes(soundResource.getSoundResID(), pitch, length);
        }
    }

    public abstract int play(int soundID, float pitch, int length);

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void stop() {
        for (Integer streamID : streamIDs) {
            soundPool.stop(streamID);
        }
    }

    public boolean soundIsLoaded(int soundID) {
        return loadedSoundIDs.contains(soundID);
    }

    @NonNull
    public static BaseSoundPool from(@NonNull String instrument, @NonNull Context context) {
        switch (instrument) {
            case Drums.SERVER_STRING:
                return new DrumsSoundPool(context);
            case Shaker.SERVER_STRING:
                return new ShakerSoundPool(context);
            default:
                return new FluteSoundPool(context);
        }
    }
}
