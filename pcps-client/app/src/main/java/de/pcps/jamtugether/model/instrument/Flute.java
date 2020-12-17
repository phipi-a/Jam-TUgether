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

        fluteSound = soundPool.load(context, R.raw.flute_sound, 1);
    }

    public void setOnLoadCompleteListener(@NonNull SoundPool.OnLoadCompleteListener onLoadCompleteListener) {
        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            onLoadCompleteListener.onLoadComplete(soundPool, sampleId, status);
            fluteSoundLoaded = true;
        });
    }

    @Override
    public void play(@NonNull Sound sound, float volume) {
        // todo
    }

    public int play(float pitch) {
        if (fluteSoundLoaded) {
            return soundPool.play(fluteSound, 1, 1, 1, 99, pitch);
        }
        Timber.w("flute sound not loaded yet");
        return 0;
    }

    public int stop(int fluteStreamingID) {
        soundPool.stop(fluteStreamingID);
        return 0;
    }

    public void release() {
        soundPool.release();
    }
}
