package de.pcps.jamtugether.ui.room.music.instrument.shaker;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.shaker.Shaker;
import de.pcps.jamtugether.model.sound.ServerSound;
import de.pcps.jamtugether.model.sound.SoundResource;
import de.pcps.jamtugether.ui.room.music.OnOwnSoundtrackChangedCallback;
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentViewModel;

public class ShakerViewModel extends InstrumentViewModel implements SensorEventListener {
    @NonNull
    private static final Shaker shaker = Shaker.getInstance();
    @NonNull
    private final MutableLiveData<Float> shakeIntensity = new MutableLiveData<>(0f);
    public void shakeIntensityReset() {
        shakeIntensity.setValue(0f);
    }
    @NonNull
    public LiveData<Float> getShakeIntensity() {
        return shakeIntensity;
    }

    public ShakerViewModel(int roomID, int userID, @NonNull OnOwnSoundtrackChangedCallback callback) {
        super(shaker, roomID, userID, callback);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private void onPause() {
        if(startedSoundtrackCreation()) {
            finishSoundtrack();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        shaker.stop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float value = (Math.abs(event.values[0])+Math.abs(event.values[1])+Math.abs(event.values[2])) / 100;
        if (value < 0) {
            value = -1 * value * 2;
        }
        if(value>1){
            value=1;
        }
        if (value >= 0.5) {
            value=value*value;
            onShakeMovement(value);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
            if (modelClass.isAssignableFrom(ShakerViewModel.class)) {
                return (T) new ShakerViewModel(roomID, userID, callback);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public void onShakeMovement(float intensity) {
        shaker.play();//TODO volume = value
        onSoundPlayed();
        shakeIntensity.setValue(intensity);
    }
    private void onSoundPlayed() {
        shaker.play();
        if (!timer.isRunning()) {
            return;
        }
        int soundDuration = SoundResource.SHAKER.getDuration();
        int startTimeMillis = (int) (System.currentTimeMillis() - startedMillis);
        int endTimeMillis = startTimeMillis + soundDuration;
        if(ownSoundtrack != null) {
            ownSoundtrack.addSound(new ServerSound(roomID, userID, Shaker.getInstance(), 0, startTimeMillis, endTimeMillis, -1));
        }
    }
}
