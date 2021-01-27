package de.pcps.jamtugether.ui.room.music.instrument.shaker;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.databinding.FragmentShakerBinding;
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentFragment;

public class ShakerFragment extends InstrumentFragment {

    private SensorManager mSensorManager;
    private Sensor accelerometerSensor;

    private ShakerViewModel shakerViewModel;

    @NonNull
    public static ShakerFragment newInstance() {
        return new ShakerFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShakerViewModel.Factory shakerViewModelFactory = new ShakerViewModel.Factory(onOwnSoundtrackChangedCallback);
        viewModel = new ViewModelProvider(this, shakerViewModelFactory).get(ShakerViewModel.class);

        // Get sensor manager
        mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);

        // Get the default sensor of specified type
        accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mSensorManager.registerListener(shakerViewModel, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        FragmentShakerBinding binding = FragmentShakerBinding.inflate(inflater, container, false);
        shakerViewModel = (ShakerViewModel) viewModel;
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(shakerViewModel);
        binding.ownSoundtrackControlsLayout.setLifecycleOwner(getViewLifecycleOwner());
        binding.ownSoundtrackControlsLayout.setViewModel(viewModel);

        shakerViewModel.getLockOrientation().observe(getViewLifecycleOwner(), lockOrientation -> {
            if(lockOrientation) {
                lockOrientation();
            } else {
                unlockOrientation();
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
            mSensorManager.registerListener(shakerViewModel, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        }
    }
}