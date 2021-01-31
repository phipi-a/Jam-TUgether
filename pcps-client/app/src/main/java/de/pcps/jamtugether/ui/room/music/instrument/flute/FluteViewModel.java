package de.pcps.jamtugether.ui.room.music.instrument.flute;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.audio.instrument.flute.Flute;
import de.pcps.jamtugether.audio.instrument.flute.FluteRecordingThread;
import de.pcps.jamtugether.audio.instrument.flute.OnAmplitudeChangedCallback;
import de.pcps.jamtugether.model.Sound;
import de.pcps.jamtugether.audio.instrument.flute.FluteSound;
import de.pcps.jamtugether.ui.room.music.OnOwnSoundtrackChangedCallback;
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentViewModel;

public class FluteViewModel extends InstrumentViewModel implements LifecycleObserver, OnAmplitudeChangedCallback {

    @NonNull
    private static final Flute flute = Flute.getInstance();

    @Nullable
    private FluteRecordingThread fluteRecordingThread;

    private boolean fragmentFocused;
    private boolean soundIsPlaying;

    private int startTimeMillis = -1;

    @NonNull
    private final MutableLiveData<Integer> pitch = new MutableLiveData<>(FluteSound.DEFAULT.getPitch());

    public FluteViewModel(@NonNull OnOwnSoundtrackChangedCallback callback) {
        super(flute, callback);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onPause() {
        fragmentFocused = false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onResume() {
        fragmentFocused = true;
    }

    public void startRecordingFlute() {
        fluteRecordingThread = new FluteRecordingThread(this);
        fluteRecordingThread.startRecording();
    }

    @Override
    public void onAmplitudeChanged(int maxAmplitude) {
        if (!fragmentFocused) {
            return;
        }
        // ignore recorder when soundtracks are playing in order to avoid sound playing issues
        if (singleSoundtrackPlayer.isPlaying() || compositeSoundtrackPlayer.isPlaying()) {
            return;
        }
        if (maxAmplitude < 10000) {
            finishSound();
        } else {
            if (!soundIsPlaying && pitch.getValue() != null) {
                flute.play(pitch.getValue(), streamID -> {
                    soundIsPlaying = streamID != 0;

                    if (recordingSoundtrack() && soundIsPlaying) {
                        startTimeMillis = (int) (System.currentTimeMillis() - startedMillis);
                    }
                });
            }
        }
    }

    private void finishSound() {
        flute.stop();
        if (startTimeMillis != -1) {
            if (ownSoundtrack != null && pitch.getValue() != null) {
                int stoppedEndTimeMillis = (int) (System.currentTimeMillis() - startedMillis);
                int completeSoundEndTimeMillis = startTimeMillis + FluteSound.from(pitch.getValue()).getDuration();
                int endTimeMillis = Math.min(stoppedEndTimeMillis, completeSoundEndTimeMillis);
                // todo remove above after loop issue is fixed
                // int endTimeMillis = (int) (System.currentTimeMillis() - startedMillis);
                ownSoundtrack.addSound(new Sound(startTimeMillis, endTimeMillis, pitch.getValue()));
            }
            startTimeMillis = -1;
        }
        soundIsPlaying = false;
    }

    @Override
    protected void finishRecordingSoundtrack() {
        finishSound();
        super.finishRecordingSoundtrack();
    }

    public void onPitchPercentageChanged(float pitchPercentage) {
        int pitch = (int) Math.floor((FluteSound.values().length - 1) * pitchPercentage);

        if (pitch >= FluteSound.C.getPitch() && pitch <= FluteSound.C_HIGH.getPitch()) {
            this.pitch.setValue(pitch);
        }
    }

    private void stopRecording() {
        if (fluteRecordingThread != null) {
            fluteRecordingThread.stopRecording();
            fluteRecordingThread = null;
        }
        flute.stop();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopRecording();
    }

    @NonNull
    public LiveData<Integer> getPitchLevel() {
        return Transformations.map(pitch, pitch -> (int) (pitch / (double) (FluteSound.values().length - 1) * 10000));
    }

    static class Factory implements ViewModelProvider.Factory {

        @NonNull
        private final OnOwnSoundtrackChangedCallback callback;

        public Factory(@NonNull OnOwnSoundtrackChangedCallback callback) {
            this.callback = callback;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(FluteViewModel.class)) {
                return (T) new FluteViewModel(callback);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
