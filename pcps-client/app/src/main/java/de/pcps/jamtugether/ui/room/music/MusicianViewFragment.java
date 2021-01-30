package de.pcps.jamtugether.ui.room.music;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.ui.room.music.instrument.drums.DrumsFragment;
import de.pcps.jamtugether.ui.room.music.instrument.flute.FluteFragment;
import de.pcps.jamtugether.ui.room.music.instrument.piano.PianoFragment;
import de.pcps.jamtugether.ui.room.music.instrument.shaker.ShakerFragment;
import de.pcps.jamtugether.ui.room.music.soundtrack.OwnSoundtrackFragment;
import de.pcps.jamtugether.databinding.FragmentMusicianViewBinding;
import de.pcps.jamtugether.utils.NavigationUtils;

public class MusicianViewFragment extends BaseFragment {

    private MusicianViewViewModel viewModel;

    @NonNull
    public static MusicianViewFragment newInstance() {
        return new MusicianViewFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MusicianViewViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMusicianViewBinding binding = FragmentMusicianViewBinding.inflate(inflater, container, false);

        if (savedInstanceState == null) {
            addSoundtrackFragment();
        }

        viewModel.getShowFluteFragment().observe(getViewLifecycleOwner(), showFluteFragment -> {
            if (showFluteFragment) {
                replaceInstrumentFragment(FluteFragment.newInstance());
                viewModel.onFluteFragmentShown();
            }
        });

        viewModel.getShowDrumsFragment().observe(getViewLifecycleOwner(), showDrumsFragment -> {
            if (showDrumsFragment) {
                replaceInstrumentFragment(DrumsFragment.newInstance());
                viewModel.onDrumsFragmentShown();
            }
        });

        viewModel.getShowShakerFragment().observe(getViewLifecycleOwner(), showShakerFragment -> {
            if (showShakerFragment) {
                replaceInstrumentFragment(ShakerFragment.newInstance());
                viewModel.onShakerFragmentShown();
            }
        });

        viewModel.getShowPianoFragment().observe(getViewLifecycleOwner(), showPianoFragment -> {
            if (showPianoFragment) {
                replaceInstrumentFragment(PianoFragment.newInstance());
                viewModel.onPianoFragmentShown();
            }
        });

        return binding.getRoot();
    }

    private void addSoundtrackFragment() {
        NavigationUtils.replaceFragment(getChildFragmentManager(), OwnSoundtrackFragment.newInstance(), R.id.own_soundtrack_fragment_container);
    }

    private void replaceInstrumentFragment(@NonNull Fragment fragment) {
        NavigationUtils.replaceFragment(getChildFragmentManager(), fragment, R.id.instrument_fragment_container);
    }
}