package de.pcps.jamtugether.ui.room.music.soundtrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.databinding.FragmentOwnSoundtrackBinding;
import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.ui.room.CompositeSoundtrackViewModel;
import de.pcps.jamtugether.ui.room.music.MusicianViewViewModel;
import de.pcps.jamtugether.ui.soundtrack.SoundtrackDataBindingUtils;
import de.pcps.jamtugether.utils.UiUtils;

public class OwnSoundtrackFragment extends BaseFragment {

    private static final String ROOM_ID_KEY = "room_id_key";
    private static final String USER_ID_KEY = "user_id_key";
    private static final String TOKEN_KEY = "token_key";

    private CompositeSoundtrackViewModel compositeSoundtrackViewModel;

    private OwnSoundtrackViewModel ownSoundtrackViewModel;

    @NonNull
    public static OwnSoundtrackFragment newInstance(int roomID, int userID, @NonNull String token) {
        OwnSoundtrackFragment fragment = new OwnSoundtrackFragment();
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
            int roomID = getArguments().getInt(ROOM_ID_KEY);
            int userID = getArguments().getInt(USER_ID_KEY);
            String token = getArguments().getString(TOKEN_KEY);

            Fragment musicianFragment = getParentFragment();
            if(musicianFragment == null) {
                return;
            }

            Fragment roomFragment = musicianFragment.getParentFragment();
            if(roomFragment == null) {
                return;
            }

            CompositeSoundtrackViewModel.Factory compositeSoundtrackViewModelFactory = new CompositeSoundtrackViewModel.Factory(roomID, userID, token);
            compositeSoundtrackViewModel = new ViewModelProvider(roomFragment, compositeSoundtrackViewModelFactory).get(CompositeSoundtrackViewModel.class);

            MusicianViewViewModel musicianViewViewModel = new ViewModelProvider(musicianFragment).get(MusicianViewViewModel.class);
            OwnSoundtrackViewModel.Factory ownSoundtrackViewModelFactory = new OwnSoundtrackViewModel.Factory(roomID, musicianViewViewModel);
            ownSoundtrackViewModel = new ViewModelProvider(this, ownSoundtrackViewModelFactory).get(OwnSoundtrackViewModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentOwnSoundtrackBinding binding = FragmentOwnSoundtrackBinding.inflate(inflater, container, false);
        binding.setViewModel(ownSoundtrackViewModel);

        SoundtrackDataBindingUtils.bindCompositeSoundtrack(binding.compositeSoundtrackLayout, compositeSoundtrackViewModel.getCompositeSoundtrack(), ownSoundtrackViewModel.getSoundtrackOnChangeCallback(), getViewLifecycleOwner());

        SoundtrackDataBindingUtils.bindOwnSoundtrack(binding.ownSoundtrackLayout, ownSoundtrackViewModel.getOwnSoundtrack(), ownSoundtrackViewModel.getSoundtrackOnChangeCallback(), getViewLifecycleOwner());

        ownSoundtrackViewModel.getShowHelpDialog().observe(getViewLifecycleOwner(), showHelpDialog -> {
            if (showHelpDialog) {
                String helpDialogTitle = ownSoundtrackViewModel.getHelpDialogTitle();
                String helpDialogMessage = ownSoundtrackViewModel.getHelpDialogMessage();
                if(helpDialogTitle == null || helpDialogMessage == null) {
                    return;
                }
                UiUtils.showInfoDialog(activity, helpDialogTitle, helpDialogMessage);
                ownSoundtrackViewModel.onHelpDialogShown();
            }
        });

        ownSoundtrackViewModel.getSoundtrackRepositoryNetworkError().observe(getViewLifecycleOwner(), networkError -> {
            if (networkError != null) {
                UiUtils.showInfoDialog(activity, networkError.getTitle(), networkError.getMessage());
                ownSoundtrackViewModel.onSoundtrackRepositoryNetworkErrorShown();
            }
        });

        return binding.getRoot();
    }
}
