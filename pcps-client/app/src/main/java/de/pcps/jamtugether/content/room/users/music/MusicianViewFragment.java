package de.pcps.jamtugether.content.room.users.music;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.content.room.users.music.instruments.drums.DrumsFragment;
import de.pcps.jamtugether.content.room.users.music.instruments.flute.FluteFragment;
import de.pcps.jamtugether.content.room.users.music.instruments.shaker.ShakerFragment;
import de.pcps.jamtugether.content.room.users.music.soundtrack.SoundtrackFragment;
import de.pcps.jamtugether.databinding.FragmentMusicianViewBinding;
import de.pcps.jamtugether.utils.NavigationUtils;

// this fragment contains soundtrack fragment AND the instrument fragment
public class MusicianViewFragment extends Fragment {

    private static final String ROOM_ID_KEY = "room_id_key";

    private int roomID;

    private Context context;

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

        addSoundtrackFragment();

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
        FragmentManager fragmentManager = getChildFragmentManager();
        NavigationUtils.replaceFragment(fragmentManager, SoundtrackFragment.newInstance(roomID, viewModel), R.id.soundtrack_fragment_container);
    }

    private void replaceInstrumentFragment(@NonNull Fragment fragment) {
        FragmentManager fragmentManager = getChildFragmentManager();
        NavigationUtils.replaceFragment(fragmentManager, fragment, R.id.instrument_fragment_container);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}