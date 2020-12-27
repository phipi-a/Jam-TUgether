package de.pcps.jamtugether.ui.room.music.instrument.flute;

import android.media.MediaRecorder;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;

import javax.inject.Inject;

import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.audio.instrument.Flute;

public class FluteViewModel extends ViewModel {

    private static final float PITCH_MIN_PERCENTAGE = 0.2f;
    private static final float PITCH_MAX_PERCENTAGE = 1f;
    private static final float PITCH_MULTIPLIER = 3f;
    private static final float PITCH_DEFAULT_PERCENTAGE = 0.3f;

    private final Flute flute = Flute.getInstance();

    private int fluteStreamingID;

    @NonNull
    private final MutableLiveData<Float> pitchPercentage = new MutableLiveData<>(PITCH_DEFAULT_PERCENTAGE);

    @NonNull
    private final MediaRecorder soundRecorder;

    @NonNull
    private final Thread soundReactThread;

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
        soundReactThread = createThread();
    }

    @NonNull
    private Thread createThread() {
        return new Thread() {
            @Override
            public void run() {
                while (!this.isInterrupted()) {
                    int maxAmplitude = soundRecorder.getMaxAmplitude();
                    if (maxAmplitude != 0) {
                        if (maxAmplitude < 10000) {
                            fluteStreamingID = flute.stop();
                        } else {
                            if (fluteStreamingID == 0 && pitchPercentage.getValue() != null) {
                                float pitch = pitchPercentage.getValue() * PITCH_MULTIPLIER;
                                fluteStreamingID = flute.play(pitch);
                            }
                        }
                    }
                }
            }
        };
    }

    public void startRecording() {
        soundRecorder.start();
        soundReactThread.start();
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

    private void stopRecording() {
        soundReactThread.interrupt();
        soundRecorder.stop();
        soundRecorder.release();
        flute.stop();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopRecording();
    }

    @NonNull
    public LiveData<Float> getPitchPercentage() {
        return pitchPercentage;
    }
}
