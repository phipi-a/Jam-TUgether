package de.pcps.jamtugether.content.room.music.instruments.flute;

import android.content.Context;
import android.media.MediaRecorder;
import android.media.SoundPool;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import de.pcps.jamtugether.R;

public class FluteViewModel extends ViewModel {
    public static float PITCH_MIN_PERCENTAGE = 0.2f;
    public static float PITCH_MAX_PERCENTAGE = 1f;
    public static float PITCH_MULTIPLIER = 3f;
    public static float PITCH_DEFAULT_PERCENTAGE = 0.3f;

    @NonNull
    private final MutableLiveData<Float> pitchPercentage = new MutableLiveData<>(PITCH_DEFAULT_PERCENTAGE);

    @NonNull
    private final MutableLiveData<Boolean> playing = new MutableLiveData<>(false);

    private MediaRecorder soundRecorder;
    private SoundPool soundPool;
    private int fluteStreamingId;
    private int fluteSoundId;
    private Thread soundReactThread;
    public void onPitchChanged(float newPitch) {
        pitchPercentage.setValue(newPitch);
    }
    public @NotNull LiveData<Float> getPitchPercentage() {
        return pitchPercentage;
    }

    public void startFlute(Context context) {
        if(!playing.getValue()) {
            soundRecorder = new MediaRecorder();
            soundRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            soundRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            soundRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            soundRecorder.setOutputFile("/dev/null");
            try {
                soundRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .build();
            fluteSoundId = soundPool.load(context, R.raw.flute_sound, 1);

            soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
                soundRecorder.start();
                playing.setValue(true);

                startSoundReact();

            });
        }
    }

    public void startSoundReact() {
        soundReactThread = new Thread() {
            @Override
            public void run() {
                while (!this.isInterrupted()) {
                    int mm = soundRecorder.getMaxAmplitude();
                    if (mm != 0) {
                        if (mm < 10000) {
                            if (fluteStreamingId != 0) {
                                soundPool.stop(fluteStreamingId);
                                fluteStreamingId = 0;
                            }
                        } else {
                            if (fluteStreamingId == 0){
                                float pitch=pitchPercentage.getValue() * PITCH_MULTIPLIER;
                                fluteStreamingId = soundPool.play(fluteSoundId, 1, 1, 1, 99, pitch);
                            }
                        }
                    }

                }

            }
        };
        soundReactThread.start();
    }

    public void stopFlute() {
        if (soundReactThread != null) {
            soundReactThread.interrupt();
            soundReactThread = null;
            soundRecorder.stop();
            soundRecorder.release();
            soundRecorder = null;
            if(fluteStreamingId!=0) {
                soundPool.stop(fluteStreamingId);
                fluteStreamingId = 0;
            }
            soundPool.release();
            soundPool = null;
            fluteSoundId = 0;
            playing.setValue(false);
        }
    }

}
