package de.pcps.jamtugether.model.instrument;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import androidx.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.instrument.base.Instrument;
import timber.log.Timber;
import de.pcps.jamtugether.model.music.sound.Sound;

@Singleton
public class Flute extends Instrument {

    @NonNull
    public static final String PREFERENCE_VALUE = "flute";

    @NonNull
    public static final String SERVER_STRING = "flute";

    @NonNull
    private final SoundPool soundPool;

    private final int fluteSound;

    private boolean fluteSoundLoaded;

    private int streamID;

    @Inject
    public Flute(@NonNull Context context) {
        super(0, R.string.instrument_flute, R.string.play_flute_help, PREFERENCE_VALUE, SERVER_STRING);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            fluteSoundLoaded = true;
        });

        fluteSound = soundPool.load(context, R.raw.flute_sound, 1);
    }

    @Override
    public void play(@NonNull Sound sound, float volume) {
        // todo
    }

    public int play(float pitch) {
        if (fluteSoundLoaded) {
            streamID = soundPool.play(fluteSound, 1, 1, 1, 99, pitch);
            return streamID;
        }
        Timber.w("flute sound not loaded yet");
        return 0;
    }

    public int stop() {
        if(streamID != 0) {
            soundPool.stop(streamID);
        }
        return 0;
    }
}
