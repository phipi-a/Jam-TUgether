package de.pcps.jamtugether.ui.room.music.instrument.piano;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.databinding.FragmentPianoBinding;
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentFragment;

public class PianoFragment extends InstrumentFragment {

    @NonNull
    public static PianoFragment newInstance() {
        return new PianoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PianoViewModel.Factory pianoViewModel = new PianoViewModel.Factory(musicianViewViewModel);
        viewModel = new ViewModelProvider(this, pianoViewModel).get(PianoViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        FragmentPianoBinding binding = FragmentPianoBinding.inflate(inflater, container, false);
        binding.ownSoundtrackControlsLayout.setLifecycleOwner(getViewLifecycleOwner());
        binding.ownSoundtrackControlsLayout.setViewModel(viewModel);
        binding.pianoView.setViewModel((PianoViewModel) viewModel);

        return binding.getRoot();
    }
}
