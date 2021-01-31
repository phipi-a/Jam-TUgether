package de.pcps.jamtugether.audio.sound;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import de.pcps.jamtugether.audio.sound.model.SoundResource;
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

    protected final int loop;

    protected float volume = 1;

    public BaseSoundPool(@NonNull Context context, int maxStreams, @NonNull SoundResource[] soundResources, int loop) {
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
        this.loop = loop;

        for (SoundResource soundRes : soundResources) {
            int soundID = soundPool.load(context, soundRes.getResource(), 1);
            soundResMap.put(soundRes.getResource(), soundID);
        }

        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> loadedSoundIDs.add(sampleId));
    }

    public int play(int soundID) {
        return soundPool.play(soundID, volume, volume, 0, loop, 1);
    }

    public void playSoundRes(int soundResID, @Nullable OnSoundPlayedCallback callback) {
        Integer soundID = soundResMap.get(soundResID);
        if (soundID == null) {
            return;
        }
        if (soundIsLoaded(soundID)) {

            new PlaySoundThread(this, soundID, streamID -> {
                if (!streamIDs.contains(streamID) && streamID != 0) {
                    streamIDs.add(streamID);
                }
                if (callback != null) {
                    callback.onSoundPlayed(streamID);
                }
            }).playSound();
        }
    }

    public void playSoundRes(int soundResID) {
        playSoundRes(soundResID, null);
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void stopSound(int streamID) {
        if (streamIDs.contains(streamID)) {
            new StopSoundThread(soundPool, streamID).stopSound();
            streamIDs.remove(Integer.valueOf(streamID));
        }
    }

    public void stopAllSounds() {
        synchronized (streamIDs) {
            for (int streamID : streamIDs) {
                new StopSoundThread(soundPool, streamID).stopSound();
            }
            streamIDs.clear();
        }
    }

    public boolean soundIsLoaded(int soundID) {
        return loadedSoundIDs.contains(soundID);
    }

    @NonNull
    public static BaseSoundPool from(@NonNull String instrumentString, @NonNull Context context) {
        Instrument instrument = Instruments.fromServer(instrumentString);
        return instrument.createSoundPool(context);
    }
}
