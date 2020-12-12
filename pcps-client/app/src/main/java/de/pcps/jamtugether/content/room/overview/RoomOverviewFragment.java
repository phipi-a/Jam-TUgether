package de.pcps.jamtugether.content.room.overview;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.navigation.fragment.NavHostFragment;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.content.BaseFragment;
import de.pcps.jamtugether.content.soundtrack.SoundtrackDataBindingUtils;
import de.pcps.jamtugether.content.soundtrack.SoundtrackItemDecoration;
import de.pcps.jamtugether.content.soundtrack.adapters.SoundtrackListAdapter;
import de.pcps.jamtugether.databinding.FragmentRoomOverviewBinding;
import de.pcps.jamtugether.utils.NavigationUtils;
import de.pcps.jamtugether.utils.UiUtils;

public abstract class RoomOverviewFragment extends BaseFragment {

    protected static final String ROOM_ID_KEY = "room_id_key";
    protected static final String TOKEN_KEY = "token_key";

    protected int roomID;
    protected String token;

    protected RoomOverviewViewModel viewModel;

    protected Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            roomID = getArguments().getInt(ROOM_ID_KEY);
            token = getArguments().getString(TOKEN_KEY);
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
        binding.allSoundtracksRecyclerView.addItemDecoration(new SoundtrackItemDecoration(context));
        viewModel.getAllSoundtracks().observe(getViewLifecycleOwner(), adapter::submitList);

        viewModel.getNetworkError().observe(getViewLifecycleOwner(), networkError -> {
            if(networkError != null) {
                AlertDialog dialog = UiUtils.createInfoDialog(context, networkError.getTitle(), networkError.getMessage());
                dialog.setOnShowListener(arg -> {
                    // todo find better way to color dialog buttons
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.primaryTextColor));
                    viewModel.onNetworkErrorShown();
                });
                dialog.show();
            }
        });

        viewModel.getLeaveRoom().observe(getViewLifecycleOwner(), leaveRoom -> {
            if(leaveRoom) {
                NavigationUtils.navigateBack(NavHostFragment.findNavController(this));
                viewModel.onLeftRoom();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @SuppressWarnings("rawtypes")
    protected abstract SoundtrackListAdapter createSoundtrackListAdapter();
}
