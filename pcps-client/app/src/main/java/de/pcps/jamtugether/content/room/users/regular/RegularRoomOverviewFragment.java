package de.pcps.jamtugether.content.room.users.regular;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.databinding.FragmentRegularRoomOverviewBinding;

// todo
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
        FragmentRegularRoomOverviewBinding binding = FragmentRegularRoomOverviewBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }
}
