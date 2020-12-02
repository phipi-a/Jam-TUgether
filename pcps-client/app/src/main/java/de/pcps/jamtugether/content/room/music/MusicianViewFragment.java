package de.pcps.jamtugether.content.room.music;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.content.room.music.instruments.drums.DrumsFragment;
import de.pcps.jamtugether.content.room.music.instruments.flute.FluteFragment;
import de.pcps.jamtugether.content.room.music.instruments.shaker.ShakerFragment;
import de.pcps.jamtugether.content.room.music.soundtrack.SoundtrackFragment;
import de.pcps.jamtugether.databinding.FragmentMusicianViewBinding;
import de.pcps.jamtugether.utils.NavigationUtils;

public class MusicianViewFragment extends Fragment {

    private static final String ROOM_ID_KEY = "room_id_key";

    private int roomID;

    private MusicianViewViewModel viewModel;

    @NonNull
    public static MusicianViewFragment newInstance(int roomID) {
        MusicianViewFragment fragment = new MusicianViewFragment();
        Bundle args = new Bundle();
        args.putInt(ROOM_ID_KEY, roomID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            roomID = getArguments().getInt(ROOM_ID_KEY);
            MusicianViewViewModel.Factory viewModelFactory = new MusicianViewViewModel.Factory(roomID);
            viewModel = new ViewModelProvider(this, viewModelFactory).get(MusicianViewViewModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMusicianViewBinding binding = FragmentMusicianViewBinding.inflate(inflater, container, false);

        if(savedInstanceState == null) {
            addSoundtrackFragment();
        }

        viewModel.getShowFluteFragment().observe(getViewLifecycleOwner(), showFluteFragment -> {
            if(showFluteFragment) {
                replaceInstrumentFragment(FluteFragment.newInstance());
                viewModel.onFluteFragmentShown();
            }
        });

        viewModel.getShowDrumsFragment().observe(getViewLifecycleOwner(), showDrumsFragment -> {
            if(showDrumsFragment) {
                replaceInstrumentFragment(DrumsFragment.newInstance());
                viewModel.onDrumsFragmentShown();
            }
        });

        viewModel.getShowShakerFragment().observe(getViewLifecycleOwner(), showShakerFragment -> {
            if(showShakerFragment) {
                replaceInstrumentFragment(ShakerFragment.newInstance());
                viewModel.onShakerFragmentShown();
            }
        });

        return binding.getRoot();
    }

    private void addSoundtrackFragment() {
        NavigationUtils.replaceFragment(getChildFragmentManager(), SoundtrackFragment.newInstance(roomID, viewModel), R.id.soundtrack_fragment_container);
    }

    private void replaceInstrumentFragment(@NonNull Fragment fragment) {
        NavigationUtils.replaceFragment(getChildFragmentManager(), fragment, R.id.instrument_fragment_container);
    }
}