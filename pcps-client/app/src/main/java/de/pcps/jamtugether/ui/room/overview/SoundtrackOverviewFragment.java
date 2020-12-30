package de.pcps.jamtugether.ui.room.overview;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.ui.room.CompositeSoundtrackViewModel;
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

public class SoundtrackOverviewFragment extends BaseFragment {

    private static final String ROOM_ID_KEY = "room_id_key";
    private static final String PASSWORD_KEY = "password_key";
    private static final String TOKEN_KEY = "token_key";
    private static final String ADMIN_KEY = "admin_key";

    private CompositeSoundtrackViewModel compositeSoundtrackViewModel;

    private SoundtrackOverviewViewModel soundtrackOverviewViewModel;

    @NonNull
    public static SoundtrackOverviewFragment newInstance(int roomID, @NonNull String password, @NonNull String token, boolean admin) {
        SoundtrackOverviewFragment fragment = new SoundtrackOverviewFragment();
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

            Fragment roomFragment = getParentFragment();

            compositeSoundtrackViewModel = new ViewModelProvider(roomFragment, new CompositeSoundtrackViewModel.Factory(roomID)).get(CompositeSoundtrackViewModel.class);

            RoomViewModel roomViewModel = new ViewModelProvider(roomFragment, new RoomViewModel.Factory(roomID, admin)).get(RoomViewModel.class);
            SoundtrackOverviewViewModel.Factory soundtrackOverviewViewModelFactory = new SoundtrackOverviewViewModel.Factory(roomID, password, token, admin, roomViewModel);
            soundtrackOverviewViewModel = new ViewModelProvider(this, soundtrackOverviewViewModelFactory).get(SoundtrackOverviewViewModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentRoomOverviewBinding binding = FragmentRoomOverviewBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(soundtrackOverviewViewModel);

        SoundtrackDataBindingUtils.bindCompositeSoundtrack(binding.compositeSoundtrackLayout.soundtrackControlsLayout, compositeSoundtrackViewModel.getCompositeSoundtrack(), soundtrackOverviewViewModel.getSoundtrackOnChangeCallback(), getViewLifecycleOwner());
        ((SoundtrackContainer) binding.compositeSoundtrackLayout.soundtrackContainer).observeCompositeSoundtrack(compositeSoundtrackViewModel.getCompositeSoundtrack(), getViewLifecycleOwner());

        binding.allSoundtracksRecyclerView.addItemDecoration(new SoundtrackItemDecoration(activity));

        final Runnable commitCallback = () -> binding.allSoundtracksRecyclerView.post(binding.allSoundtracksRecyclerView::invalidateItemDecorations);

        soundtrackOverviewViewModel.getAllSoundtracks().observe(getViewLifecycleOwner(), soundtracks -> soundtrackOverviewViewModel.onNewSoundtracks(soundtracks));

        soundtrackOverviewViewModel.getUserIsAdmin().observe(getViewLifecycleOwner(), admin -> {
            if (admin) {
                AdminSoundtrackListAdapter adapter = new AdminSoundtrackListAdapter(soundtrackOverviewViewModel.getSoundtrackOnChangeCallback(), soundtrackOverviewViewModel, getViewLifecycleOwner());
                binding.allSoundtracksRecyclerView.setAdapter(adapter);
                soundtrackOverviewViewModel.getAllSoundtracks().observe(getViewLifecycleOwner(), allSoundtracks -> adapter.submitList(allSoundtracks, commitCallback));
            } else {
                RegularSoundtrackListAdapter adapter = new RegularSoundtrackListAdapter(soundtrackOverviewViewModel.getSoundtrackOnChangeCallback(), getViewLifecycleOwner());
                binding.allSoundtracksRecyclerView.setAdapter(adapter);
                soundtrackOverviewViewModel.getAllSoundtracks().observe(getViewLifecycleOwner(), allSoundtracks -> adapter.submitList(allSoundtracks, commitCallback));
            }
        });

        soundtrackOverviewViewModel.getSoundtrackRepositoryNetworkError().observe(getViewLifecycleOwner(), networkError -> {
            if (networkError != null) {
                UiUtils.showInfoDialog(activity, networkError.getTitle(), networkError.getMessage());
                soundtrackOverviewViewModel.onSoundtrackRepositoryNetworkErrorShown();
            }
        });

        soundtrackOverviewViewModel.getNetworkError().observe(getViewLifecycleOwner(), networkError -> {
            if (networkError != null) {
                UiUtils.showInfoDialog(activity, networkError.getTitle(), networkError.getMessage());
                soundtrackOverviewViewModel.onNetworkErrorShown();
            }
        });

        soundtrackOverviewViewModel.getShowSoundtrackDeletionConfirmDialog().observe(getViewLifecycleOwner(), showSoundtrackDeletionConfirmDialog -> {
            if (showSoundtrackDeletionConfirmDialog) {
                UiUtils.showConfirmationDialog(activity, R.string.delete_soundtrack, R.string.delete_soundtrack_confirmation, soundtrackOverviewViewModel::onSoundtrackDeletionConfirmButtonClicked);
                soundtrackOverviewViewModel.onSoundtrackDeletionConfirmDialogShown();
            }
        });

        soundtrackOverviewViewModel.getShowRoomDeletionConfirmDialog().observe(getViewLifecycleOwner(), showRoomDeletionConfirmDialog -> {
            if (showRoomDeletionConfirmDialog) {
                UiUtils.showConfirmationDialog(activity, R.string.delete_room, R.string.delete_room_confirmation, soundtrackOverviewViewModel::onRoomDeletionConfirmButtonClicked);
                soundtrackOverviewViewModel.onRoomDeletionConfirmDialogShown();
            }
        });

        soundtrackOverviewViewModel.getNavigateBack().observe(getViewLifecycleOwner(), leaveRoom -> {
            if (leaveRoom) {
                NavigationUtils.navigateBack(NavHostFragment.findNavController(this));
                soundtrackOverviewViewModel.onRoomDeleted();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.activity = (AppCompatActivity) context;
    }
}
