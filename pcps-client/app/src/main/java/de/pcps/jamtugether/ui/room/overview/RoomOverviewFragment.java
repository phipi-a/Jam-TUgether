package de.pcps.jamtugether.ui.room.overview;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.ui.room.AdminStatusChangeCallback;
import de.pcps.jamtugether.ui.room.RoomViewModel;
import de.pcps.jamtugether.ui.soundtrack.SoundtrackDataBindingUtils;
import de.pcps.jamtugether.ui.soundtrack.SoundtrackItemDecoration;
import de.pcps.jamtugether.ui.soundtrack.adapters.AdminSoundtrackListAdapter;
import de.pcps.jamtugether.ui.soundtrack.adapters.RegularSoundtrackListAdapter;
import de.pcps.jamtugether.databinding.FragmentRoomOverviewBinding;
import de.pcps.jamtugether.ui.soundtrack.views.SoundtrackContainer;
import de.pcps.jamtugether.utils.NavigationUtils;
import de.pcps.jamtugether.utils.UiUtils;
import timber.log.Timber;

public class RoomOverviewFragment extends BaseFragment {

    private static final String ROOM_ID_KEY = "room_id_key";
    private static final String PASSWORD_KEY = "password_key";
    private static final String TOKEN_KEY = "token_key";
    private static final String ADMIN_KEY = "admin_key";

    private RoomOverviewViewModel viewModel;

    private Context context;

    @NonNull
    public static RoomOverviewFragment newInstance(int roomID, @NonNull String password, @NonNull String token, boolean admin) {
        RoomOverviewFragment fragment = new RoomOverviewFragment();
        Bundle args = new Bundle();

        args.putInt(ROOM_ID_KEY, roomID);
        args.putString(PASSWORD_KEY, password);
        args.putString(TOKEN_KEY, token);
        args.putBoolean(ADMIN_KEY, admin);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int roomID = getArguments().getInt(ROOM_ID_KEY);
            String password = getArguments().getString(PASSWORD_KEY);
            String token = getArguments().getString(TOKEN_KEY);
            boolean admin = getArguments().getBoolean(ADMIN_KEY);

            if (getParentFragment() != null) {
                AdminStatusChangeCallback adminStatusChangeCallback = new ViewModelProvider(getParentFragment(), new RoomViewModel.Factory(admin)).get(RoomViewModel.class);
                RoomOverviewViewModel.Factory viewModelFactory = new RoomOverviewViewModel.Factory(roomID, password, token, admin, adminStatusChangeCallback);
                viewModel = new ViewModelProvider(this, viewModelFactory).get(RoomOverviewViewModel.class);
            } else {
                Timber.e("parent fragment is null (shouldn't happen)");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentRoomOverviewBinding binding = FragmentRoomOverviewBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        SoundtrackDataBindingUtils.bindCompositeSoundtrack(binding.compositeSoundtrackLayout.soundtrackControlsLayout, viewModel.getCompositeSoundtrack(), getViewLifecycleOwner());
        ((SoundtrackContainer) binding.compositeSoundtrackLayout.soundtrackContainer).observeCompositeSoundtrack(viewModel.getCompositeSoundtrack(), getViewLifecycleOwner());

        binding.allSoundtracksRecyclerView.addItemDecoration(new SoundtrackItemDecoration(context));

        final Runnable commitCallback = () -> binding.allSoundtracksRecyclerView.post(binding.allSoundtracksRecyclerView::invalidateItemDecorations);

        viewModel.getAdmin().observe(getViewLifecycleOwner(), admin -> {
            if (admin) {
                AdminSoundtrackListAdapter adapter = new AdminSoundtrackListAdapter(viewModel, getViewLifecycleOwner());
                binding.allSoundtracksRecyclerView.setAdapter(adapter);
                viewModel.getAllSoundtracks().observe(getViewLifecycleOwner(), allSoundtracks -> adapter.submitList(allSoundtracks, commitCallback));
            } else {
                RegularSoundtrackListAdapter adapter = new RegularSoundtrackListAdapter(getViewLifecycleOwner());
                binding.allSoundtracksRecyclerView.setAdapter(adapter);
                viewModel.getAllSoundtracks().observe(getViewLifecycleOwner(), allSoundtracks -> adapter.submitList(allSoundtracks, commitCallback));
            }
        });

        viewModel.getNetworkError().observe(getViewLifecycleOwner(), networkError -> {
            if (networkError != null) {
                UiUtils.showInfoDialog(context, networkError.getTitle(), networkError.getMessage());
                viewModel.onNetworkErrorShown();
            }
        });

        viewModel.getShowSoundtrackDeletionConfirmDialog().observe(getViewLifecycleOwner(), showSoundtrackDeletionConfirmDialog -> {
            if(showSoundtrackDeletionConfirmDialog) {
                UiUtils.showConfirmationDialog(context, R.string.delete_soundtrack, R.string.delete_soundtrack_confirmation, viewModel::onSoundtrackDeletionConfirmButtonClicked);
                viewModel.onSoundtrackDeletionConfirmDialogShown();
            }
        });

        viewModel.getShowRoomDeletionConfirmDialog().observe(getViewLifecycleOwner(), showRoomDeletionConfirmDialog -> {
            if (showRoomDeletionConfirmDialog) {
                UiUtils.showConfirmationDialog(context, R.string.delete_room, R.string.delete_room_confirmation, viewModel::onRoomDeletionConfirmButtonClicked);
                viewModel.onRoomDeletionConfirmDialogShown();
            }
        });

        viewModel.getLeaveRoom().observe(getViewLifecycleOwner(), leaveRoom -> {
            if (leaveRoom) {
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
}
