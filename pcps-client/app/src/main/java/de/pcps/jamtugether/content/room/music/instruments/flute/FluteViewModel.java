package de.pcps.jamtugether.content.room.music.instruments.flute;

import android.content.Context;
import android.media.MediaRecorder;
import android.media.SoundPool;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;

import de.pcps.jamtugether.R;

public class FluteViewModel extends ViewModel {

    private static final float PITCH_MIN_PERCENTAGE = 0.2f;
    private static final float PITCH_MAX_PERCENTAGE = 1f;
    private static final float PITCH_MULTIPLIER = 3f;
    private static final float PITCH_DEFAULT_PERCENTAGE = 0.3f;

    @NonNull
    private final MutableLiveData<Float> pitchPercentage = new MutableLiveData<>(PITCH_DEFAULT_PERCENTAGE);

    @NonNull
    private final MediaRecorder soundRecorder;

    @NonNull
    private final SoundPool soundPool;

    @NonNull
    private final Thread soundReactThread;

    private int fluteStreamingID;
    private int fluteSoundID;

    public FluteViewModel() {
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

        soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        soundReactThread = createThread();
    }

    @NonNull
    private Thread createThread() {
        return new Thread() {
            @Override
            public void run() {
                while (!this.isInterrupted()) {
                    int mm = soundRecorder.getMaxAmplitude();
                    if (mm != 0) {
                        if (mm < 10000) {
                            if (fluteStreamingID != 0) {
                                soundPool.stop(fluteStreamingID);
                                fluteStreamingID = 0;
                            }
                        } else {
                            if (fluteStreamingID == 0) {
                                float pitch = pitchPercentage.getValue() * PITCH_MULTIPLIER;
                                fluteStreamingID = soundPool.play(fluteSoundID, 1, 1, 1, 99, pitch);
                            }
                        }
                    }
                }
            }
        };
    }

    public void startRecording(@NonNull Context context) {
        fluteSoundID = soundPool.load(context, R.raw.flute_sound, 1);
        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            soundRecorder.start();
            soundReactThread.start();
        });
    }

    public void onPitchChanged(float newPitch) {
        if (newPitch < PITCH_MIN_PERCENTAGE) {
            newPitch = PITCH_MIN_PERCENTAGE;
        }
        if (newPitch > PITCH_MAX_PERCENTAGE) {
            newPitch = PITCH_MAX_PERCENTAGE;
        }
        pitchPercentage.setValue(newPitch);
    }

    private void stopFlute() {
        if (soundReactThread != null) {
            soundReactThread.interrupt();
            soundRecorder.stop();
            soundRecorder.release();
            if (fluteStreamingID != 0) {
                soundPool.stop(fluteStreamingID);
                fluteStreamingID = 0;
            }
            soundPool.release();
            fluteSoundID = 0;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopFlute();
    }

    @NonNull
    public LiveData<Float> getPitchPercentage() {
        return pitchPercentage;
    }
}
