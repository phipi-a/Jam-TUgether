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
import de.pcps.jamtugether.model.User;
import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.ui.room.music.instrument.drums.DrumsFragment;
import de.pcps.jamtugether.ui.room.music.instrument.flute.FluteFragment;
import de.pcps.jamtugether.ui.room.music.instrument.shaker.ShakerFragment;
import de.pcps.jamtugether.ui.room.music.soundtrack.OwnSoundtrackFragment;
import de.pcps.jamtugether.databinding.FragmentMusicianViewBinding;
import de.pcps.jamtugether.utils.NavigationUtils;

public class MusicianViewFragment extends BaseFragment {

    private static final String ROOM_ID_KEY = "room_id_key";
    private static final String USER_KEY = "user_key";
    private static final String TOKEN_KEY = "token_key";

    private int roomID;

    private User user;

    private String token;

    private MusicianViewViewModel viewModel;

    @NonNull
    public static MusicianViewFragment newInstance(int roomID, @NonNull User user, @NonNull String token) {
        MusicianViewFragment fragment = new MusicianViewFragment();
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
            roomID = getArguments().getInt(ROOM_ID_KEY);
            user = (User) getArguments().getSerializable(USER_KEY);
            token = getArguments().getString(TOKEN_KEY);

            viewModel = new ViewModelProvider(this).get(MusicianViewViewModel.class);
        }
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
                replaceInstrumentFragment(FluteFragment.newInstance(roomID, user, token));
                viewModel.onFluteFragmentShown();
            }
        });

        viewModel.getShowDrumsFragment().observe(getViewLifecycleOwner(), showDrumsFragment -> {
            if (showDrumsFragment) {
                replaceInstrumentFragment(DrumsFragment.newInstance(roomID, user, token));
                viewModel.onDrumsFragmentShown();
            }
        });

        viewModel.getShowShakerFragment().observe(getViewLifecycleOwner(), showShakerFragment -> {
            if (showShakerFragment) {
                replaceInstrumentFragment(ShakerFragment.newInstance(roomID, user, token));
                viewModel.onShakerFragmentShown();
            }
        });

        return binding.getRoot();
    }

    private void addSoundtrackFragment() {
        NavigationUtils.replaceFragment(getChildFragmentManager(), OwnSoundtrackFragment.newInstance(roomID, user.getID(), token), R.id.own_soundtrack_fragment_container);
    }

    private void replaceInstrumentFragment(@NonNull Fragment fragment) {
        NavigationUtils.replaceFragment(getChildFragmentManager(), fragment, R.id.instrument_fragment_container);
    }
}