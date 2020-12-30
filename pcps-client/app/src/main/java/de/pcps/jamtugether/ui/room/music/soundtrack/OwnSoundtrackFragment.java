package de.pcps.jamtugether.ui.room.music.soundtrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.ui.room.CompositeSoundtrackViewModel;
import de.pcps.jamtugether.ui.room.music.MusicianViewViewModel;
import de.pcps.jamtugether.ui.soundtrack.SoundtrackDataBindingUtils;
import de.pcps.jamtugether.databinding.FragmentSoundtrackBinding;
import de.pcps.jamtugether.ui.soundtrack.views.SoundtrackContainer;
import de.pcps.jamtugether.utils.UiUtils;

public class OwnSoundtrackFragment extends BaseFragment {

    private static final String ROOM_ID_KEY = "room_id_key";
    private static final String TOKEN_KEY = "token_key";

    private CompositeSoundtrackViewModel compositeSoundtrackViewModel;

    private OwnSoundtrackViewModel ownSoundtrackViewModel;

    @NonNull
    public static OwnSoundtrackFragment newInstance(int roomID, @NonNull String token) {
        OwnSoundtrackFragment fragment = new OwnSoundtrackFragment();
        Bundle args = new Bundle();
        args.putInt(ROOM_ID_KEY, roomID);
        args.putString(TOKEN_KEY, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int roomID = getArguments().getInt(ROOM_ID_KEY);
            String token = getArguments().getString(TOKEN_KEY);

            Fragment roomFragment = getParentFragment().getParentFragment();

            CompositeSoundtrackViewModel.Factory compositeSoundtrackViewModelFactory = new CompositeSoundtrackViewModel.Factory(roomID);
            compositeSoundtrackViewModel = new ViewModelProvider(roomFragment, compositeSoundtrackViewModelFactory).get(CompositeSoundtrackViewModel.class);
            MusicianViewViewModel musicianViewViewModel = new ViewModelProvider(roomFragment, new MusicianViewViewModel.Factory(roomID, token)).get(MusicianViewViewModel.class);
            OwnSoundtrackViewModel.Factory ownSoundtrackViewModelFactory = new OwnSoundtrackViewModel.Factory(roomID, musicianViewViewModel);
            ownSoundtrackViewModel = new ViewModelProvider(this, ownSoundtrackViewModelFactory).get(OwnSoundtrackViewModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSoundtrackBinding binding = FragmentSoundtrackBinding.inflate(inflater, container, false);
        binding.setViewModel(ownSoundtrackViewModel);

        SoundtrackDataBindingUtils.bindCompositeSoundtrack(binding.compositeSoundtrackLayout.soundtrackControlsLayout, compositeSoundtrackViewModel.getCompositeSoundtrack(), ownSoundtrackViewModel.getSoundtrackOnChangeCallback(), getViewLifecycleOwner());
        ((SoundtrackContainer) binding.compositeSoundtrackLayout.soundtrackContainer).observeCompositeSoundtrack(compositeSoundtrackViewModel.getCompositeSoundtrack(), getViewLifecycleOwner());

        SoundtrackDataBindingUtils.bindSingleSoundtrack(binding.ownSoundtrackLayout.soundtrackControlsLayout, ownSoundtrackViewModel.getOwnSoundtrack(), ownSoundtrackViewModel.getSoundtrackOnChangeCallback(), getViewLifecycleOwner());
        ((SoundtrackContainer) binding.ownSoundtrackLayout.soundtrackContainer).observeSingleSoundtrack(ownSoundtrackViewModel.getOwnSoundtrack(), getViewLifecycleOwner());

        ownSoundtrackViewModel.getShowHelpDialog().observe(getViewLifecycleOwner(), showHelpDialog -> {
            if (showHelpDialog) {
                UiUtils.showInfoDialog(activity, ownSoundtrackViewModel.getHelpDialogTitle(), ownSoundtrackViewModel.getHelpDialogMessage());
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
