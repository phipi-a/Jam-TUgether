package de.pcps.jamtugether.model.instrument;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.instrument.base.Instrument;

import de.pcps.jamtugether.model.music.sound.Sound;
import timber.log.Timber;

@Singleton
public class Drums extends Instrument {

    @NonNull
    public static final String PREFERENCE_VALUE = "drums";

    @NonNull
    public static final String SERVER_STRING = "drums";

    /*
     this is necessary in order to play a lot of
     sounds in a short period of time because these sounds
     start before another one ends
     */;
    private static final int SOUND_POOL_MAX_STREAMS = 100;

    @NonNull
    private final SoundPool soundPool;

    private final int snareSound;
    private final int kickSound;
    private final int hatSound;
    private final int cymbalSound;

    private boolean snareSoundLoaded;
    private boolean kickSoundLoaded;
    private boolean hatSoundLoaded;
    private boolean cymbalSoundLoaded;

    private final List<Integer> streamIDs;

    @Inject
    public Drums(@NonNull Context context) {
        super(1, R.string.instrument_drums, R.string.play_drums_help, PREFERENCE_VALUE, SERVER_STRING);
        this.streamIDs = new ArrayList<>();

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(SOUND_POOL_MAX_STREAMS)
                .setAudioAttributes(audioAttributes)
                .build();

        snareSound = soundPool.load(context, R.raw.drum_snare, 1);
        kickSound = soundPool.load(context, R.raw.drum_kick, 1);
        hatSound = soundPool.load(context, R.raw.drum_hat, 1);
        cymbalSound = soundPool.load(context, R.raw.drum_cymbal, 1);

        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            if (sampleId == snareSound) {
                snareSoundLoaded = true;
            } else if (sampleId == kickSound) {
                kickSoundLoaded = true;
            } else if (sampleId == hatSound) {
                hatSoundLoaded = true;
            } else if (sampleId == cymbalSound) {
                cymbalSoundLoaded = true;
            }
        });
    }

    @Override
    public void play(@NonNull Sound sound, float volume) {
        // todo
    }

    public void playSnare() {
        if (snareSoundLoaded) {
            playSound(snareSound);
        } else {
            Timber.w("snare sound not loaded yet");
        }
    }

    public void playKick() {
        if (kickSoundLoaded) {
            playSound(kickSound);
        } else {
            Timber.w("kick sound not loaded yet");
        }
    }

    public void playHat() {
        if (hatSoundLoaded) {
            playSound(hatSound);
        } else {
            Timber.w("hat sound not loaded yet");
        }
    }

    public void playCymbal() {
        if (cymbalSoundLoaded) {
            playSound(cymbalSound);
        } else {
            Timber.w("cymbal sound not loaded yet");
        }
    }

    private void playSound(int sound) {
        int streamID = soundPool.play(sound, 1, 1, 0, 0, 1);

        if(!streamIDs.contains(streamID) && streamID != 0) {
            streamIDs.add(streamID);
        }
    }

    public void stop() {
        for(int streamID : streamIDs) {
            soundPool.stop(streamID);
        }
    }
}
