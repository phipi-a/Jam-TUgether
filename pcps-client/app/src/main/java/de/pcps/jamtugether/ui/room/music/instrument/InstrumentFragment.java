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
import timber.log.Timber;

public abstract class InstrumentFragment extends BaseFragment {

    protected static final String ROOM_ID_KEY = "room_id_key";
    protected static final String USER_ID_KEY = "user_id_key";
    protected static final String TOKEN_KEY = "token_key";

    protected int roomID;
    protected int userID;

    protected MusicianViewViewModel musicianViewViewModel;
    protected InstrumentViewModel instrumentViewModel;
    private CompositeSoundtrackViewModel compositeSoundtrackViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            roomID = getArguments().getInt(ROOM_ID_KEY);
            userID = getArguments().getInt(USER_ID_KEY);
            String token = getArguments().getString(TOKEN_KEY);

            Fragment musicianFragment = getParentFragment();
            Fragment roomFragment = musicianFragment.getParentFragment();

            MusicianViewViewModel.Factory musicianViewViewModelFactory = new MusicianViewViewModel.Factory(roomID, userID, token);
            musicianViewViewModel = new ViewModelProvider(musicianFragment, musicianViewViewModelFactory).get(MusicianViewViewModel.class);

            CompositeSoundtrackViewModel.Factory compositeSoundtrackViewModelFactory = new CompositeSoundtrackViewModel.Factory(roomID);
            compositeSoundtrackViewModel = new ViewModelProvider(roomFragment, compositeSoundtrackViewModelFactory).get(CompositeSoundtrackViewModel.class);
        }
    }

    protected void observeCompositeSoundtrack() {
        compositeSoundtrackViewModel.getCompositeSoundtrack().observe(getViewLifecycleOwner(), compositeSoundtrack -> instrumentViewModel.onCompositeSoundtrackChanged(compositeSoundtrack));
    }
}
