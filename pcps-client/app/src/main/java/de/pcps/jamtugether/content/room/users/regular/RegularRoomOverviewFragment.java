package de.pcps.jamtugether.content.room.users.regular;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.content.soundtrack.adapters.RegularSoundtrackListAdapter;
import de.pcps.jamtugether.databinding.FragmentRoomOverviewBinding;
import de.pcps.jamtugether.databinding.ViewSoundtrackControlsBinding;

public class RegularRoomOverviewFragment extends Fragment {

    private static final String ROOM_ID_KEY = "room_id_key";

    private RegularRoomOverviewViewModel viewModel;

    @NonNull
    public static RegularRoomOverviewFragment newInstance(int roomID) {
        RegularRoomOverviewFragment fragment = new RegularRoomOverviewFragment();
        Bundle args = new Bundle();
        args.putInt(ROOM_ID_KEY, roomID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            int roomID = getArguments().getInt(ROOM_ID_KEY);
            RegularRoomOverviewViewModel.Factory viewModelFactory = new RegularRoomOverviewViewModel.Factory(roomID);
            viewModel = new ViewModelProvider(this, viewModelFactory).get(RegularRoomOverviewViewModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentRoomOverviewBinding binding = FragmentRoomOverviewBinding.inflate(inflater, container, false);
        binding.setRoomID(viewModel.getRoomID());

        ViewSoundtrackControlsBinding compositeSoundtrackControls = binding.compositeSoundtrackLayout.soundtrackControlsLayout;
        viewModel.getCompositeSoundtrack().observe(getViewLifecycleOwner(), compositeSoundtrackControls::setSoundtrack);
        compositeSoundtrackControls.setOnChangeListener(viewModel);
        compositeSoundtrackControls.setLifecycleOwner(getViewLifecycleOwner());

        binding.compositeSoundtrackLayout.soundtrackView.observeSoundtrack(viewModel.getCompositeSoundtrack(), getViewLifecycleOwner());

        RegularSoundtrackListAdapter adapter = new RegularSoundtrackListAdapter(viewModel, getViewLifecycleOwner());
        binding.allSoundtracksRecyclerView.setAdapter(adapter);
        viewModel.getAllSoundtracks().observe(getViewLifecycleOwner(), adapter::submitList);

        return binding.getRoot();
    }
}
