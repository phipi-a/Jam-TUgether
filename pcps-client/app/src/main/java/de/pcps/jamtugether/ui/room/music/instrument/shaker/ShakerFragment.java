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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.audio.instrument.shaker.Shaker;
import de.pcps.jamtugether.databinding.FragmentDrumsBinding;
import de.pcps.jamtugether.databinding.FragmentShakerBinding;
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentFragment;
import de.pcps.jamtugether.ui.room.music.instrument.drums.DrumsViewModel;
import de.pcps.jamtugether.ui.room.music.instrument.flute.FluteViewModel;
import de.pcps.jamtugether.utils.UiUtils;

public class ShakerFragment extends InstrumentFragment {

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
        ShakerViewModel shakerViewModel = (ShakerViewModel) instrumentViewModel;
        binding.setViewModel(shakerViewModel);
        binding.ownSoundtrackControlsLayout.setLifecycleOwner(getViewLifecycleOwner());
        binding.ownSoundtrackControlsLayout.setViewModel(instrumentViewModel);

        observeCompositeSoundtrack();

        // Get sensor manager
        SensorManager mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        // Get the default sensor of specified type
        Sensor mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(shakerViewModel, mLight,
                SensorManager.SENSOR_DELAY_GAME);
        Vibrator v= (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        shakerViewModel.getShakeIntensity().observe(getViewLifecycleOwner(), intensity -> {
            if (intensity>0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot((int) (50 * intensity), VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    v.vibrate(50);
                }
                shakerViewModel.shakeIntensityReset();
            }
        });
        return binding.getRoot();
    }

}