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
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentFragment;
import de.pcps.jamtugether.ui.room.music.instrument.flute.view.FluteView;
import de.pcps.jamtugether.utils.UiUtils;

public class FluteFragment extends InstrumentFragment {

    private static final int REQUEST_MICROPHONE = 1;

    @NonNull
    public static FluteFragment newInstance() {
        return new FluteFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FluteViewModel.Factory fluteViewModelFactory = new FluteViewModel.Factory(musicianViewViewModel);
        viewModel = new ViewModelProvider(this, fluteViewModelFactory).get(FluteViewModel.class);
        getLifecycle().addObserver((FluteViewModel) viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        FragmentFluteBinding binding = FragmentFluteBinding.inflate(inflater, container, false);
        FluteViewModel fluteViewModel = (FluteViewModel) viewModel;
        binding.fluteView.setLifecycleOwner(getViewLifecycleOwner());
        binding.fluteView.setViewModel(fluteViewModel);
        binding.fluteView.setMusicianViewViewModel(musicianViewViewModel);
        binding.ownSoundtrackControlsLayout.setLifecycleOwner(getViewLifecycleOwner());
        binding.ownSoundtrackControlsLayout.setViewModel(viewModel);

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
                FluteViewModel fluteViewModel = (FluteViewModel) viewModel;
                fluteViewModel.startRecording();
            } else {
                UiUtils.showInfoDialog(context, R.string.no_permission_microphone_error_title, R.string.no_permission_microphone_error_message);
            }
        }
    }
}
