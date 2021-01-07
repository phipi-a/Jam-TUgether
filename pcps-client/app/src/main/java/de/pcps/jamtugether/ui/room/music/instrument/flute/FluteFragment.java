package de.pcps.jamtugether.ui.room.music.instrument.flute;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.databinding.FragmentFluteBinding;
import de.pcps.jamtugether.model.User;
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentFragment;
import de.pcps.jamtugether.utils.UiUtils;

public class FluteFragment extends InstrumentFragment {

    private static final int REQUEST_MICROPHONE = 1;

    @NonNull
    public static FluteFragment newInstance(int roomID, @NonNull User user, @NonNull String token) {
        FluteFragment fragment = new FluteFragment();
        Bundle args = new Bundle();
        args.putInt(ROOM_ID_KEY, roomID);
        args.putSerializable(USER_KEY, user);
        args.putString(TOKEN_KEY, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            FluteViewModel.Factory fluteViewModelFactory = new FluteViewModel.Factory(roomID, user, token, onOwnSoundtrackChangedCallback);
            instrumentViewModel = new ViewModelProvider(this, fluteViewModelFactory).get(FluteViewModel.class);
            getLifecycle().addObserver((FluteViewModel) instrumentViewModel);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        FragmentFluteBinding binding = FragmentFluteBinding.inflate(inflater, container, false);
        FluteViewModel fluteViewModel = (FluteViewModel) instrumentViewModel;
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(fluteViewModel);
        binding.ownSoundtrackControlsLayout.setLifecycleOwner(getViewLifecycleOwner());
        binding.ownSoundtrackControlsLayout.setViewModel(instrumentViewModel);

        if (savedInstanceState == null) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_MICROPHONE);
            } else {
                fluteViewModel.startRecording();
            }
        }

        return binding.getRoot();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_MICROPHONE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                FluteViewModel fluteViewModel = (FluteViewModel) instrumentViewModel;
                fluteViewModel.startRecording();
            } else {
                UiUtils.showInfoDialog(context, R.string.no_permission_microphone_error_title, R.string.no_permission_microphone_error_message);
            }
        }
    }
}
