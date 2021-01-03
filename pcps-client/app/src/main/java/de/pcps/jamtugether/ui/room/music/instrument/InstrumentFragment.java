package de.pcps.jamtugether.ui.room.music.instrument;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.ui.room.CompositeSoundtrackViewModel;
import de.pcps.jamtugether.ui.room.music.MusicianViewViewModel;

public abstract class InstrumentFragment extends BaseFragment {

    protected static final String ROOM_ID_KEY = "room_id_key";
    protected static final String USER_ID_KEY = "user_id_key";
    protected static final String TOKEN_KEY = "token_key";

    protected int userID;
    protected String token;

    protected MusicianViewViewModel musicianViewViewModel;
    protected InstrumentViewModel instrumentViewModel;
    private CompositeSoundtrackViewModel compositeSoundtrackViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            int roomID = getArguments().getInt(ROOM_ID_KEY);
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

            musicianViewViewModel = new ViewModelProvider(musicianFragment).get(MusicianViewViewModel.class);

            CompositeSoundtrackViewModel.Factory compositeSoundtrackViewModelFactory = new CompositeSoundtrackViewModel.Factory(roomID);
            compositeSoundtrackViewModel = new ViewModelProvider(roomFragment, compositeSoundtrackViewModelFactory).get(CompositeSoundtrackViewModel.class);
        }
    }

    protected void observeCompositeSoundtrack() {
        compositeSoundtrackViewModel.getCompositeSoundtrack().observe(getViewLifecycleOwner(), compositeSoundtrack -> instrumentViewModel.onCompositeSoundtrackChanged(compositeSoundtrack));
    }
}
