package de.pcps.jamtugether.content.room.users.regular;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.content.soundtrack.Soundtrack;
import de.pcps.jamtugether.content.soundtrack.adapters.RegularSoundtrackListAdapter;
import de.pcps.jamtugether.databinding.FragmentRoomOverviewBinding;
import de.pcps.jamtugether.databinding.ViewSoundtrackControlsBinding;

// todo maybe put some code together with AdminRoomOverviewFragment
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

        Soundtrack.OnChangeListener onChangeListener = viewModel;
        LifecycleOwner lifecycleOwner = getViewLifecycleOwner();
        LiveData<Drawable> playPauseButtonDrawable = viewModel.getPlayPauseButtonDrawable();
        LiveData<Integer> stopButtonVisibility = viewModel.getStopButtonVisibility();

        ViewSoundtrackControlsBinding compositeSoundtrackControls = binding.compositeSoundtrackLayout.soundtrackControlsLayout;
        viewModel.getCompositeSoundtrack().observe(lifecycleOwner, compositeSoundtrackControls::setSoundtrack);
        compositeSoundtrackControls.setOnChangeListener(onChangeListener);
        compositeSoundtrackControls.setLifecycleOwner(lifecycleOwner);
        compositeSoundtrackControls.setPlayPauseButtonDrawable(playPauseButtonDrawable);
        compositeSoundtrackControls.setStopButtonVisibility(stopButtonVisibility);
        binding.compositeSoundtrackLayout.soundtrackView.observeSoundtrack(viewModel.getCompositeSoundtrack(), lifecycleOwner);
        RegularSoundtrackListAdapter adapter = new RegularSoundtrackListAdapter(onChangeListener, playPauseButtonDrawable, stopButtonVisibility, lifecycleOwner);
        binding.allSoundtracksRecyclerView.setAdapter(adapter);
        viewModel.getAllSoundtracks().observe(getViewLifecycleOwner(), adapter::submitList);

        return binding.getRoot();
    }
}
