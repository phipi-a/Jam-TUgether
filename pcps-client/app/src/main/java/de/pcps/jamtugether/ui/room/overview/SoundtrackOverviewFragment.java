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

import javax.inject.Inject;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.databinding.FragmentSoundtrackOverviewBinding;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.ui.room.CompositeSoundtrackViewModel;
import de.pcps.jamtugether.ui.soundtrack.SoundtrackDataBindingUtils;
import de.pcps.jamtugether.ui.soundtrack.SoundtrackItemDecoration;
import de.pcps.jamtugether.ui.soundtrack.adapters.AdminSoundtrackListAdapter;
import de.pcps.jamtugether.ui.soundtrack.adapters.RegularSoundtrackListAdapter;
import de.pcps.jamtugether.utils.NavigationUtils;
import de.pcps.jamtugether.utils.UiUtils;

public class SoundtrackOverviewFragment extends BaseFragment {

    @Inject
    Soundtrack.OnChangeCallback onChangeCallback;

    private SoundtrackOverviewViewModel viewModel;

    private CompositeSoundtrackViewModel compositeSoundtrackViewModel;

    @NonNull
    public static SoundtrackOverviewFragment newInstance() {
        return new SoundtrackOverviewFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInjector.inject(this);

        Fragment roomFragment = getParentFragment();
        if (roomFragment == null) {
            return;
        }

        viewModel = new ViewModelProvider(this).get(SoundtrackOverviewViewModel.class);

        compositeSoundtrackViewModel = new ViewModelProvider(roomFragment).get(CompositeSoundtrackViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSoundtrackOverviewBinding binding = FragmentSoundtrackOverviewBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        SoundtrackDataBindingUtils.bindCompositeSoundtrack(binding.compositeSoundtrackLayout, compositeSoundtrackViewModel.getCompositeSoundtrack(), onChangeCallback, getViewLifecycleOwner());

        binding.allSoundtracksRecyclerView.addItemDecoration(new SoundtrackItemDecoration(activity));

        final Runnable invalidateItemDecorations = () -> binding.allSoundtracksRecyclerView.post(binding.allSoundtracksRecyclerView::invalidateItemDecorations);

        viewModel.getUserIsAdmin().observe(getViewLifecycleOwner(), admin -> {
            if (admin) {
                AdminSoundtrackListAdapter adapter = new AdminSoundtrackListAdapter(onChangeCallback, viewModel, getViewLifecycleOwner());
                binding.allSoundtracksRecyclerView.setAdapter(adapter);
                viewModel.getAllSoundtracks().observe(getViewLifecycleOwner(), allSoundtracks -> adapter.submitList(allSoundtracks, invalidateItemDecorations));
            } else {
                Integer userID = viewModel.getUserID();
                if(userID == null) {
                    return;
                }
                RegularSoundtrackListAdapter adapter = new RegularSoundtrackListAdapter(userID, onChangeCallback, viewModel, getViewLifecycleOwner());
                binding.allSoundtracksRecyclerView.setAdapter(adapter);
                viewModel.getAllSoundtracks().observe(getViewLifecycleOwner(), allSoundtracks -> adapter.submitList(allSoundtracks, invalidateItemDecorations));
            }
        });

        viewModel.getSoundtrackRepositoryNetworkError().observe(getViewLifecycleOwner(), networkError -> {
            if (networkError != null) {
                UiUtils.showInfoDialog(activity, networkError.getTitle(), networkError.getMessage());
                viewModel.onSoundtrackRepositoryNetworkErrorShown();
            }
        });

        viewModel.getNetworkError().observe(getViewLifecycleOwner(), networkError -> {
            if (networkError != null) {
                UiUtils.showInfoDialog(activity, networkError.getTitle(), networkError.getMessage());
                viewModel.onNetworkErrorShown();
            }
        });

        viewModel.getShowSoundtrackDeletionConfirmDialog().observe(getViewLifecycleOwner(), showSoundtrackDeletionConfirmDialog -> {
            if (showSoundtrackDeletionConfirmDialog) {
                UiUtils.showConfirmationDialog(activity, R.string.delete_soundtrack, R.string.delete_soundtrack_confirmation, viewModel::onSoundtrackDeletionConfirmButtonClicked);
                viewModel.onSoundtrackDeletionConfirmDialogShown();
            }
        });

        viewModel.getShowRoomDeletionConfirmDialog().observe(getViewLifecycleOwner(), showRoomDeletionConfirmDialog -> {
            if (showRoomDeletionConfirmDialog) {
                UiUtils.showConfirmationDialog(activity, R.string.delete_room, R.string.delete_room_confirmation, viewModel::onRoomDeletionConfirmButtonClicked);
                viewModel.onRoomDeletionConfirmDialogShown();
            }
        });

        viewModel.getNavigateBack().observe(getViewLifecycleOwner(), leaveRoom -> {
            if (leaveRoom) {
                NavigationUtils.navigateBack(NavHostFragment.findNavController(this));
                viewModel.onNavigatedBack();
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
