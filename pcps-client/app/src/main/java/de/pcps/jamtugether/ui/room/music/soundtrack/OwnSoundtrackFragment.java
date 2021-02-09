package de.pcps.jamtugether.ui.room.music.soundtrack;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Inject;

import de.pcps.jamtugether.api.errors.ForbiddenAccessError;
import de.pcps.jamtugether.api.errors.RoomDeletedError;
import de.pcps.jamtugether.databinding.FragmentOwnSoundtrackBinding;
import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.ui.room.music.MusicianViewViewModel;
import de.pcps.jamtugether.ui.room.music.soundtrack.help.DrumsHelpFragment;
import de.pcps.jamtugether.ui.room.music.soundtrack.help.FluteHelpFragment;
import de.pcps.jamtugether.ui.room.music.soundtrack.help.PianoHelpFragment;
import de.pcps.jamtugether.ui.room.music.soundtrack.help.ShakerHelpFragment;
import de.pcps.jamtugether.ui.soundtrack.SoundtrackDataBindingUtils;
import de.pcps.jamtugether.utils.UiUtils;

public class OwnSoundtrackFragment extends BaseFragment {

    @Inject
    Soundtrack.OnChangeCallback onChangeCallback;

    private OwnSoundtrackViewModel viewModel;

    private MusicianViewViewModel musicianViewViewModel;

    @NonNull
    public static OwnSoundtrackFragment newInstance() {
        return new OwnSoundtrackFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInjector.inject(this);

        Fragment musicianFragment = getParentFragment();
        if (musicianFragment == null) {
            return;
        }

        musicianViewViewModel = new ViewModelProvider(musicianFragment).get(MusicianViewViewModel.class);

        OwnSoundtrackViewModel.Factory ownSoundtrackViewModelFactory = new OwnSoundtrackViewModel.Factory(musicianViewViewModel, musicianViewViewModel);
        viewModel = new ViewModelProvider(this, ownSoundtrackViewModelFactory).get(OwnSoundtrackViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentOwnSoundtrackBinding binding = FragmentOwnSoundtrackBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setViewModel(viewModel);
        binding.setMusicianViewViewModel(musicianViewViewModel);

        SoundtrackDataBindingUtils.bindCompositeSoundtrack(binding.compositeSoundtrackLayout, viewModel.getCompositeSoundtrack(), onChangeCallback, getViewLifecycleOwner());

        SoundtrackDataBindingUtils.bindOwnSoundtrack(binding.ownSoundtrackLayout, musicianViewViewModel.getOwnSoundtrack(), onChangeCallback, getViewLifecycleOwner());

        viewModel.getShowFluteHelpDialog().observe(getViewLifecycleOwner(), showHelpDialog -> {
            if (showHelpDialog) {
                FluteHelpFragment.newInstance().show(getChildFragmentManager(), "");
                viewModel.onFluteHelpDialogShown();
            }
        });

        viewModel.getShowDrumsHelpDialog().observe(getViewLifecycleOwner(), showHelpDialog -> {
            if (showHelpDialog) {
                DrumsHelpFragment.newInstance().show(getChildFragmentManager(), "");
                viewModel.onDrumsHelpDialogShown();
            }
        });

        viewModel.getShowShakerHelpDialog().observe(getViewLifecycleOwner(), showHelpDialog -> {
            if (showHelpDialog) {
                ShakerHelpFragment.newInstance().show(getChildFragmentManager(), "");
                viewModel.onShakerHelpDialogShown();
            }
        });

        viewModel.getShowPianoHelpDialog().observe(getViewLifecycleOwner(), showHelpDialog -> {
            if (showHelpDialog) {
                PianoHelpFragment.newInstance().show(getChildFragmentManager(), "");
                viewModel.onPianoHelpDialogShown();
            }
        });

        viewModel.getSoundtrackRepositoryNetworkError().observe(getViewLifecycleOwner(), networkError -> {
            if (networkError != null && !(networkError instanceof RoomDeletedError) && !(networkError instanceof ForbiddenAccessError)) {
                UiUtils.showInfoDialog(context, networkError.getTitle(), networkError.getMessage());
                viewModel.onSoundtrackRepositoryNetworkErrorShown();
            }
        });

        return binding.getRoot();
    }
}
