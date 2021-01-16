package de.pcps.jamtugether.ui.room.music.instrument.flute;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.audio.instrument.flute.Flute;
import de.pcps.jamtugether.audio.instrument.flute.FluteRecordingThread;
import de.pcps.jamtugether.audio.instrument.flute.OnAmplitudeChangedCallback;
import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.model.sound.flute.FluteSound;
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

    private int pitch = FluteSound.DEFAULT.getPitch();

    public FluteViewModel(@NonNull OnOwnSoundtrackChangedCallback callback) {
        super(flute, callback);
    }

    @Override
    public void finishSoundtrack() {
        finishSound();
        super.finishSoundtrack();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onPause() {
        fragmentFocused = false;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onResume() {
        fragmentFocused = true;
    }

    public void startRecording() {
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
            if (!soundIsPlaying) {
                flute.play(pitch, streamID -> {
                    soundIsPlaying = streamID != 0;

                    if (startedSoundtrackCreation() && soundIsPlaying) {
                        startTimeMillis = (int) (System.currentTimeMillis() - startedMillis);
                    }
                });
            }
        }
    }

    private void finishSound() {
        flute.stop();
        if (startTimeMillis != -1) {
            int endTimeMillis = (int) (System.currentTimeMillis() - startedMillis);
            if (ownSoundtrack != null) {
                ownSoundtrack.addSound(new Sound(startTimeMillis, endTimeMillis, pitch));
            }
            startTimeMillis = -1;
        }
        soundIsPlaying = false;
    }

    public void onPitchChanged(int pitch) {
        this.pitch = pitch;
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
