package de.pcps.jamtugether.ui.room.music.instrument;

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
import de.pcps.jamtugether.ui.room.music.OnOwnSoundtrackChangedCallback;
import de.pcps.jamtugether.utils.UiUtils;

public abstract class InstrumentFragment extends BaseFragment {

    protected static final String ROOM_ID_KEY = "room_id_key";
    protected static final String USER_ID_KEY = "user_id_key";
    protected static final String TOKEN_KEY = "token_key";

    protected int roomID;
    protected int userID;

    protected String token;

    protected OnOwnSoundtrackChangedCallback onOwnSoundtrackChangedCallback;

    private CompositeSoundtrackViewModel compositeSoundtrackViewModel;

    protected InstrumentViewModel instrumentViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            roomID = getArguments().getInt(ROOM_ID_KEY);
            userID = getArguments().getInt(USER_ID_KEY);
            token = getArguments().getString(TOKEN_KEY);

            Fragment musicianFragment = getParentFragment();
            if(musicianFragment == null) {
                return;
            }
            Fragment roomFragment = musicianFragment.getParentFragment();
            if(roomFragment == null) {
                return;
            }

            onOwnSoundtrackChangedCallback = new ViewModelProvider(musicianFragment).get(MusicianViewViewModel.class);

            CompositeSoundtrackViewModel.Factory compositeSoundtrackViewModelFactory = new CompositeSoundtrackViewModel.Factory(roomID, userID, token);
            compositeSoundtrackViewModel = new ViewModelProvider(roomFragment, compositeSoundtrackViewModelFactory).get(CompositeSoundtrackViewModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        compositeSoundtrackViewModel.getCompositeSoundtrack().observe(getViewLifecycleOwner(), compositeSoundtrack -> instrumentViewModel.onCompositeSoundtrackChanged(compositeSoundtrack));

        instrumentViewModel.getNetworkError().observe(getViewLifecycleOwner(), networkError -> {
            if(networkError != null) {
                UiUtils.showInfoDialog(activity, networkError.getTitle(), networkError.getMessage());
                instrumentViewModel.onNetworkErrorShown();
            }
        });

        return view;
    }
}
