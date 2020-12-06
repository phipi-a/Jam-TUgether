package de.pcps.jamtugether.content.room.overview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.content.soundtrack.SoundtrackDataBindingUtils;
import de.pcps.jamtugether.content.soundtrack.adapters.SoundtrackListAdapter;
import de.pcps.jamtugether.databinding.FragmentRoomOverviewBinding;

public abstract class RoomOverviewFragment extends Fragment {

    protected static final String ROOM_ID_KEY = "room_id_key";
    protected static final String TOKEN_KEY = "token_key";

    protected RoomOverviewViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            int roomID = getArguments().getInt(ROOM_ID_KEY);
            String token = getArguments().getString(TOKEN_KEY);
            RoomOverviewViewModel.Factory viewModelFactory = new RoomOverviewViewModel.Factory(roomID, token);
            viewModel = new ViewModelProvider(this, viewModelFactory).get(RoomOverviewViewModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentRoomOverviewBinding binding = FragmentRoomOverviewBinding.inflate(inflater, container, false);
        binding.setRoomID(viewModel.getRoomID());

        SoundtrackDataBindingUtils.bind(binding.compositeSoundtrackLayout.soundtrackControlsLayout, viewModel.getCompositeSoundtrack(), viewModel, getViewLifecycleOwner());
        binding.compositeSoundtrackLayout.soundtrackView.observeSoundtrack(viewModel.getCompositeSoundtrack(), getViewLifecycleOwner());

        SoundtrackListAdapter adapter = createSoundtrackListAdapter();
        binding.allSoundtracksRecyclerView.setAdapter(adapter);
        viewModel.getAllSoundtracks().observe(getViewLifecycleOwner(), adapter::submitList);

        return binding.getRoot();
    }

    @SuppressWarnings("rawtypes")
    protected abstract SoundtrackListAdapter createSoundtrackListAdapter();
}
