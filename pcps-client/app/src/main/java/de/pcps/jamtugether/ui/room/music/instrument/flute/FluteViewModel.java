package de.pcps.jamtugether.ui.room.music.instrument.flute;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.audio.instrument.flute.Flute;
import de.pcps.jamtugether.audio.instrument.flute.FluteRecordingThread;
import de.pcps.jamtugether.audio.instrument.flute.OnAmplitudeChangedCallback;
import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.ui.room.music.OnOwnSoundtrackChangedCallback;
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentViewModel;

import static de.pcps.jamtugether.audio.instrument.flute.Flute.PITCH_DEFAULT_PERCENTAGE;
import static de.pcps.jamtugether.audio.instrument.flute.Flute.PITCH_MAX_PERCENTAGE;
import static de.pcps.jamtugether.audio.instrument.flute.Flute.PITCH_MIN_PERCENTAGE;

public class FluteViewModel extends InstrumentViewModel implements OnAmplitudeChangedCallback {

    @NonNull
    private static final Flute flute = Flute.getInstance();

    @NonNull
    private final MutableLiveData<Float> pitchPercentage = new MutableLiveData<>(PITCH_DEFAULT_PERCENTAGE);

    @Nullable
    private FluteRecordingThread fluteRecordingThread;

    private boolean fragmentFocused;

    private boolean soundIsPlaying;

    private int currentStartTimeMillis = -1;
    private int currentPitch = -1;

    public FluteViewModel(int roomID, int userID, @NonNull OnOwnSoundtrackChangedCallback callback) {
        super(flute, roomID, userID, callback);
    }

    @Override
    public void finishSoundtrack() {
        finishSound();
        super.finishSoundtrack();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onPause() {
        fragmentFocused = false;
        if (startedSoundtrackCreation()) {
            finishSoundtrack();
        }
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
            Float pitchPercentage = this.pitchPercentage.getValue();
            if (!soundIsPlaying && pitchPercentage != null) {
                flute.play(pitchPercentage * 100, streamID -> {
                    soundIsPlaying = streamID != 0;

                    if (startedSoundtrackCreation() && soundIsPlaying) {
                        currentStartTimeMillis = (int) (System.currentTimeMillis() - startedMillis);
                        currentPitch = (int) (pitchPercentage * 100);
                    }
                });
            }
        }
    }

    private void finishSound() {
        flute.stop();
        if (currentStartTimeMillis != -1 && currentPitch != -1) {
            int endTimeMillis = (int) (System.currentTimeMillis() - startedMillis);
            if(ownSoundtrack != null) {
                ownSoundtrack.addSound(new Sound(currentStartTimeMillis, endTimeMillis, currentPitch));
            }
            currentStartTimeMillis = -1;
            currentPitch = -1;
        }
        soundIsPlaying = false;
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
        if (fluteRecordingThread != null) {
            fluteRecordingThread.stopRecording();
            fluteRecordingThread = null;
        }
        flute.stop();
    }

    @NonNull
    public LiveData<Float> getPitchPercentage() {
        return pitchPercentage;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopRecording();
    }

    static class Factory implements ViewModelProvider.Factory {

        private final int roomID;
        private final int userID;

        @NonNull
        private final OnOwnSoundtrackChangedCallback callback;

        public Factory(int roomID, int userID, @NonNull OnOwnSoundtrackChangedCallback callback) {
            this.roomID = roomID;
            this.userID = userID;
            this.callback = callback;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(FluteViewModel.class)) {
                return (T) new FluteViewModel(roomID, userID, callback);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
