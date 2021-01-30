package de.pcps.jamtugether.ui.room.music.instrument.drums;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.databinding.FragmentDrumsBinding;
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentFragment;

public class DrumsFragment extends InstrumentFragment {

    @NonNull
    public static DrumsFragment newInstance() {
        return new DrumsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DrumsViewModel.Factory drumsViewModelFactory = new DrumsViewModel.Factory(musicianViewViewModel);
        viewModel = new ViewModelProvider(this, drumsViewModelFactory).get(DrumsViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        FragmentDrumsBinding binding = FragmentDrumsBinding.inflate(inflater, container, false);
        binding.setViewModel((DrumsViewModel) viewModel);
        binding.ownSoundtrackControlsLayout.setLifecycleOwner(getViewLifecycleOwner());
        binding.ownSoundtrackControlsLayout.setViewModel(viewModel);

        return binding.getRoot();
    }
}
