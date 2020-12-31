package de.pcps.jamtugether.ui.room.music.instrument.flute;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import de.pcps.jamtugether.audio.instrument.flute.Flute;
import de.pcps.jamtugether.audio.instrument.flute.FluteRecordingThread;
import de.pcps.jamtugether.audio.instrument.flute.OnAmplitudeChangedCallback;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.sound.ServerSound;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.timer.JamTimer;
import de.pcps.jamtugether.ui.room.music.OnOwnSoundtrackChangedCallback;
import de.pcps.jamtugether.utils.TimeUtils;
import timber.log.Timber;

import static de.pcps.jamtugether.audio.instrument.flute.Flute.PITCH_DEFAULT_PERCENTAGE;
import static de.pcps.jamtugether.audio.instrument.flute.Flute.PITCH_MAX_PERCENTAGE;
import static de.pcps.jamtugether.audio.instrument.flute.Flute.PITCH_MIN_PERCENTAGE;

public class FluteViewModel extends ViewModel implements OnAmplitudeChangedCallback, JamTimer.OnTickCallback {

    @Inject
    Application application;

    @NonNull
    private final Flute flute = Flute.getInstance();

    @NonNull
    private final MutableLiveData<Float> pitchPercentage = new MutableLiveData<>(PITCH_DEFAULT_PERCENTAGE);

    private FluteRecordingThread fluteRecordingThread;

    private final int roomID;
    private final int userID;

    @NonNull
    private final OnOwnSoundtrackChangedCallback callback;

    private SingleSoundtrack ownSoundtrack;

    @NonNull
    private final MutableLiveData<Boolean> startedCreatingOwnSoundtrack = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Long> timerMillis = new MutableLiveData<>();

    @NonNull
    private final JamTimer timer = new JamTimer(this, Soundtrack.MAX_TIME);

    private boolean soundIsPlaying;

    private long startedMillis;

    private int currentStartTimeMillis = -1;
    private int currentPitch = -1;

    public FluteViewModel(int roomID, int userID, @NonNull OnOwnSoundtrackChangedCallback callback) {
        AppInjector.inject(this);
        this.roomID = roomID;
        this.userID = userID;
        this.callback = callback;
    }

    public void onCreateOwnSoundtrackButtonClicked() {
        boolean started = startedCreatingOwnSoundtrack.getValue();
        if (started) {
            onFinishOwnSoundtrack();
        } else {
            timerMillis.setValue(-1L);
            ownSoundtrack = new SingleSoundtrack(userID, Flute.getInstance());
            ownSoundtrack.loadSounds(application.getApplicationContext());
            startedCreatingOwnSoundtrack.setValue(true);
        }
    }

    public void startRecording() {
        fluteRecordingThread = new FluteRecordingThread(this);
        fluteRecordingThread.startRecording();
    }

    @Override
    public void onAmplitudeChanged(int maxAmplitude) {
        Timber.d("onAmplitudeChanged()");
        // todo if player is playing, return
        if (maxAmplitude < 10000) {
            finishSound();
        } else {
            Float pitchPercentage = this.pitchPercentage.getValue();
            if (!soundIsPlaying && pitchPercentage != null) {
                int streamID = flute.play(pitchPercentage * 100);
                soundIsPlaying = streamID != 0;

                if (startedCreatingOwnSoundtrack.getValue() && soundIsPlaying) {
                    if (ownSoundtrack.isEmpty()) {
                        Timber.d("ownSoundtrack empty");
                        startedMillis = System.currentTimeMillis();
                        timer.start();
                        Timber.d("timer started");
                    }
                    currentStartTimeMillis = (int) (System.currentTimeMillis() - startedMillis);
                    currentPitch = (int) (pitchPercentage * 100);
                }
            }
        }
    }

    @Override
    public void onTicked(long millis) {
        timerMillis.postValue(millis);
    }

    @Override
    public void onFinished() {
        onFinishOwnSoundtrack();
    }

    private void finishSound() {
        flute.stop();
        if (currentStartTimeMillis != -1 && currentPitch != -1) {
            int endTimeMillis = (int) (System.currentTimeMillis() - startedMillis);
            ownSoundtrack.addSound(new ServerSound(roomID, userID, Flute.getInstance(), 0, currentStartTimeMillis, endTimeMillis, currentPitch));
            currentStartTimeMillis = -1;
            currentPitch = -1;
        }
        soundIsPlaying = false;
    }

    private void onFinishOwnSoundtrack() {
        finishSound();
        timer.stop();
        callback.onOwnSoundtrackChanged(ownSoundtrack);
        startedCreatingOwnSoundtrack.setValue(false);
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

    @NonNull
    public LiveData<Boolean> getStartedCreatingOwnSoundtrack() {
        return startedCreatingOwnSoundtrack;
    }

    @NonNull
    public LiveData<String> getTimerText() {
        return Transformations.map(timerMillis, millis -> {
            if (millis == -1L) {
                return "";
            }
            return TimeUtils.formatTimerSecondMinutes(millis);
        });
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
