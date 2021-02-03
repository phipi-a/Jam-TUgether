package de.pcps.jamtugether.ui.room.overview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.api.errors.RoomDeletedError;
import de.pcps.jamtugether.databinding.FragmentSoundtrackOverviewBinding;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.ui.base.views.dialogs.ConfirmationDialog;
import de.pcps.jamtugether.ui.room.overview.admin.AdminSettingsFragment;
import de.pcps.jamtugether.ui.soundtrack.SoundtrackDataBindingUtils;
import de.pcps.jamtugether.ui.soundtrack.SoundtrackItemDecoration;
import de.pcps.jamtugether.ui.soundtrack.adapters.AdminSoundtrackListAdapter;
import de.pcps.jamtugether.ui.soundtrack.adapters.RegularSoundtrackListAdapter;
import de.pcps.jamtugether.utils.UiUtils;

public class SoundtrackOverviewFragment extends BaseFragment {

    @Inject
    Soundtrack.OnChangeCallback onChangeCallback;

    private SoundtrackOverviewViewModel viewModel;

    @NonNull
    public static SoundtrackOverviewFragment newInstance() {
        return new SoundtrackOverviewFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInjector.inject(this);

        viewModel = new ViewModelProvider(this).get(SoundtrackOverviewViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSoundtrackOverviewBinding binding = FragmentSoundtrackOverviewBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);

        SoundtrackDataBindingUtils.bindCompositeSoundtrack(binding.compositeSoundtrackLayout, viewModel.getCompositeSoundtrack(), onChangeCallback, getViewLifecycleOwner());

        binding.allSoundtracksRecyclerView.addItemDecoration(new SoundtrackItemDecoration(activity));

        final Runnable invalidateItemDecorations = () -> binding.allSoundtracksRecyclerView.post(binding.allSoundtracksRecyclerView::invalidateItemDecorations);

        viewModel.getUserIsAdmin().observe(getViewLifecycleOwner(), admin -> {
            if (admin) {
                AdminSoundtrackListAdapter adapter = new AdminSoundtrackListAdapter(onChangeCallback, viewModel, getViewLifecycleOwner());
                binding.allSoundtracksRecyclerView.setAdapter(adapter);
                viewModel.getAllSoundtracks().observe(getViewLifecycleOwner(), allSoundtracks -> adapter.submitList(allSoundtracks, invalidateItemDecorations));
            } else {
                Integer userID = viewModel.getUserID();
                if (userID == null) {
                    return;
                }
                RegularSoundtrackListAdapter adapter = new RegularSoundtrackListAdapter(userID, onChangeCallback, viewModel, getViewLifecycleOwner());
                binding.allSoundtracksRecyclerView.setAdapter(adapter);
                viewModel.getAllSoundtracks().observe(getViewLifecycleOwner(), allSoundtracks -> adapter.submitList(allSoundtracks, invalidateItemDecorations));
            }
        });

        viewModel.getCompositionNetworkError().observe(getViewLifecycleOwner(), networkError -> {
            if (networkError != null && !(networkError instanceof RoomDeletedError)) {
                if (!viewModel.getCompositionNetworkErrorShown()) {
                    UiUtils.showInfoDialog(context, networkError.getTitle(), networkError.getMessage());
                    viewModel.onCompositionNetworkErrorShown();
                }
            }
        });

        viewModel.getShowSoundtrackDeletionConfirmDialog().observe(getViewLifecycleOwner(), showSoundtrackDeletionConfirmDialog -> {
            if (showSoundtrackDeletionConfirmDialog) {
                UiUtils.showConfirmationDialog(context, R.string.delete_soundtrack, R.string.delete_soundtrack_confirmation, new ConfirmationDialog.OnButtonClickListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        viewModel.onSoundtrackDeletionConfirmButtonClicked();
                    }

                    @Override
                    public void onNegativeButtonClicked() { }
                });
                viewModel.onSoundtrackDeletionConfirmDialogShown();
            }
        });

        viewModel.getNetworkError().observe(getViewLifecycleOwner(), networkError -> {
            if (networkError != null) {
                UiUtils.showInfoDialog(context, networkError.getTitle(), networkError.getMessage());
                viewModel.onNetworkErrorShown();
            }
        });

        viewModel.getShowNotAdminDialog().observe(getViewLifecycleOwner(), showNotAdminDialog -> {
            if (showNotAdminDialog) {
                UiUtils.showInfoDialog(context, R.string.not_admin_dialog_title, R.string.not_admin_dialog_message);
                viewModel.onNotAdminDialogShown();
            }
        });

        viewModel.getShowAdminSettingsFragment().observe(getViewLifecycleOwner(), showAdminOptionsFragment -> {
            if (showAdminOptionsFragment) {
                AdminSettingsFragment.newInstance().show(getChildFragmentManager(), "");
                viewModel.onAdminSettingsFragmentShown();
            }
        });

        return binding.getRoot();
    }
}
