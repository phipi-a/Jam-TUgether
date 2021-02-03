package de.pcps.jamtugether.audio.instrument.flute;

import android.media.MediaRecorder;

import androidx.annotation.NonNull;

import java.io.IOException;

public class FluteRecordingThread extends Thread {

    private boolean stop = false;

    @NonNull
    private final MediaRecorder soundRecorder;

    @NonNull
    private final OnAmplitudeChangedCallback callback;

    public FluteRecordingThread(@NonNull OnAmplitudeChangedCallback callback) {
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
        this.callback = callback;
    }

    @Override
    public void run() {
        while (!stop) {
            int maxAmplitude = soundRecorder.getMaxAmplitude();
            if (maxAmplitude != 0) {
                callback.onAmplitudeChanged(maxAmplitude);
            }
        }
    }

    public void startRecording() {
        soundRecorder.start();
        start();
    }

    public void stopRecording() {
        stop = true;
        soundRecorder.stop();
        soundRecorder.release();
    }

    public interface OnAmplitudeChangedCallback {

        void onAmplitudeChanged(int maxAmplitude);
    }
}
