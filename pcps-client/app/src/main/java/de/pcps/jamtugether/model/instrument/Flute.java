package de.pcps.jamtugether.model.instrument;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.instrument.base.Instrument;

public class Flute extends Instrument {

    @Nullable
    private static Flute instance;

    private static final int soundResourceID = R.raw.flute_sound;

    private SoundPool soundPool;

    private int fluteSoundID;

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

        //soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        fluteSoundID = soundPool.load(context, soundResourceID, 1);
        soundPool.setOnLoadCompleteListener(onLoadCompleteListener);
    }

    public int play(float pitch) {
        return soundPool.play(fluteSoundID, 1, 1, 1, 99, pitch);
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
