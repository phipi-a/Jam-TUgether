package de.pcps.jamtugether.ui.room.music.instrument.shaker;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.databinding.FragmentShakerBinding;
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentFragment;

public class ShakerFragment extends InstrumentFragment {

    private SensorManager mSensorManager;
    private Sensor accelerometerSensor;
    private ShakerViewModel shakerViewModel;

    @NonNull
    public static ShakerFragment newInstance(int roomID, int userID, @NonNull String token) {
        ShakerFragment fragment = new ShakerFragment();
        Bundle args = new Bundle();
        args.putInt(ROOM_ID_KEY, roomID);
        args.putInt(USER_ID_KEY, userID);
        args.putString(TOKEN_KEY, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ShakerViewModel.Factory shakerViewModelFactory = new ShakerViewModel.Factory(roomID, userID, musicianViewViewModel);
            instrumentViewModel = new ViewModelProvider(this, shakerViewModelFactory).get(ShakerViewModel.class);
            getLifecycle().addObserver(instrumentViewModel);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentShakerBinding binding = FragmentShakerBinding.inflate(inflater, container, false);
        shakerViewModel = (ShakerViewModel) instrumentViewModel;
        binding.setViewModel(shakerViewModel);
        binding.ownSoundtrackControlsLayout.setLifecycleOwner(getViewLifecycleOwner());
        binding.ownSoundtrackControlsLayout.setViewModel(instrumentViewModel);

        observeCompositeSoundtrack();

        // Get sensor manager
        mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        // Get the default sensor of specified type
        accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(shakerViewModel, accelerometerSensor,
                SensorManager.SENSOR_DELAY_GAME);
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        shakerViewModel.getShakeIntensity().observe(getViewLifecycleOwner(), intensity -> {
            if (intensity > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot((int) (50 * intensity), VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(50);
                }
                shakerViewModel.shakeIntensityReset();
                binding.ivShaker.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake));
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (accelerometerSensor != null) {
            mSensorManager.unregisterListener(shakerViewModel);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (accelerometerSensor != null) {
            mSensorManager.registerListener(shakerViewModel, accelerometerSensor,
                    SensorManager.SENSOR_DELAY_GAME);
        }
    }
}