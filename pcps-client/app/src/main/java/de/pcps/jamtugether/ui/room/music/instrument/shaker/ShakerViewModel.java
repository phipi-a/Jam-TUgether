package de.pcps.jamtugether.ui.room.music.instrument.shaker;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.audio.instrument.shaker.Shaker;
import de.pcps.jamtugether.model.sound.Sound;
import de.pcps.jamtugether.model.sound.SoundResource;
import de.pcps.jamtugether.ui.room.music.OnOwnSoundtrackChangedCallback;
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentViewModel;

public class ShakerViewModel extends InstrumentViewModel implements SensorEventListener {

    @NonNull
    private static final Shaker shaker = Shaker.getInstance();

    @NonNull
    private final Vibrator vibrator;

    @NonNull
    private final MutableLiveData<Float> shakeIntensity = new MutableLiveData<>(0f);

    public ShakerViewModel(int userID, @NonNull OnOwnSoundtrackChangedCallback callback) {
        super(shaker, userID, callback);
        this.vibrator = (Vibrator) application.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onPause() {
        if (startedSoundtrackCreation()) {
            finishSoundtrack();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float value = (Math.abs(event.values[0]) + Math.abs(event.values[1]) + Math.abs(event.values[2])) / 100;
        if (value < 0) {
            value = -1 * value * 2;
        }
        if (value > 1) {
            value = 1;
        }
        if (value >= 0.5) {
            value = value * value;
            onShake(value);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }

    private void onShake(float intensity) {
        playSound();
        shakeIntensity.setValue(intensity);
        if(intensity > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot((int) (50 * intensity), VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(50);
            }
        }
    }

    public void onShakeAnimationStarted() {
        shakeIntensity.setValue(0f);
    }

    private void playSound() {
        shaker.play();
        if (!timer.isRunning()) {
            return;
        }

        int soundDuration = SoundResource.SHAKER.getDuration();
        int startTimeMillis = (int) (System.currentTimeMillis() - startedMillis);
        int endTimeMillis = startTimeMillis + soundDuration;
        if (ownSoundtrack != null) {
            ownSoundtrack.addSound(new Sound(startTimeMillis, endTimeMillis, -1));
        }
    }

    @NonNull
    public LiveData<Float> getShakeIntensity() {
        return shakeIntensity;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        shaker.stop();
    }

    static class Factory implements ViewModelProvider.Factory {

        private final int userID;

        @NonNull
        private final OnOwnSoundtrackChangedCallback callback;

        public Factory(int userID, @NonNull OnOwnSoundtrackChangedCallback callback) {
            this.userID = userID;
            this.callback = callback;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ShakerViewModel.class)) {
                return (T) new ShakerViewModel(userID, callback);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
