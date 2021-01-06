package de.pcps.jamtugether.ui.room.music.instrument.drums;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.databinding.FragmentDrumsBinding;
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentFragment;

public class DrumsFragment extends InstrumentFragment {

    @NonNull
    public static DrumsFragment newInstance(int roomID, int userID, @NonNull String token) {
        DrumsFragment fragment = new DrumsFragment();
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
            DrumsViewModel.Factory drumsViewModelFactory = new DrumsViewModel.Factory(roomID, userID, token, onOwnSoundtrackChangedCallback);
            instrumentViewModel = new ViewModelProvider(this, drumsViewModelFactory).get(DrumsViewModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        FragmentDrumsBinding binding = FragmentDrumsBinding.inflate(inflater, container, false);
        binding.setViewModel((DrumsViewModel) instrumentViewModel);
        binding.ownSoundtrackControlsLayout.setLifecycleOwner(getViewLifecycleOwner());
        binding.ownSoundtrackControlsLayout.setViewModel(instrumentViewModel);

        return binding.getRoot();
    }
}
