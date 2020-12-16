package de.pcps.jamtugether.model.instrument;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.instrument.base.Instrument;
import timber.log.Timber;
import de.pcps.jamtugether.model.music.sound.Sound;

public class Flute extends Instrument {

    @Nullable
    private static Flute instance;

    private SoundPool soundPool;

    private int fluteSound;

    private boolean fluteSoundLoaded;

    public Flute() {
        super(0, R.string.instrument_flute, R.string.play_flute_help, "flute", "flute");
    }

    public void prepare(@NonNull Context context, @NonNull SoundPool.OnLoadCompleteListener onLoadCompleteListener) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            onLoadCompleteListener.onLoadComplete(soundPool, sampleId, status);
            fluteSoundLoaded = true;
        });
        fluteSound = soundPool.load(context, R.raw.flute_sound, 1);
    }

    @Override
    public void play(@NonNull Sound sound) {
        if(sound.getInstrument() != this) {
            return;
        }
        // todo
    }

    public int play(float pitch) {
        if(fluteSoundLoaded) {
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

    @NonNull
    public static Flute getInstance() {
        if (instance == null) {
            instance = new Flute();
        }
        return instance;
    }
}