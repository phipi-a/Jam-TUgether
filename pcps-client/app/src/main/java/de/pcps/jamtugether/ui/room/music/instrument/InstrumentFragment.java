package de.pcps.jamtugether.ui.room.music.instrument;

import android.content.SharedPreferences;
import android.icu.text.StringSearch;
import android.os.Bundle;
import android.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.storage.Preferences;
import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.ui.room.music.MusicianViewViewModel;
import de.pcps.jamtugether.ui.room.music.OnOwnSoundtrackChangedCallback;
import de.pcps.jamtugether.utils.UiUtils;

public abstract class InstrumentFragment extends BaseFragment {

    protected InstrumentViewModel viewModel;

    protected OnOwnSoundtrackChangedCallback onOwnSoundtrackChangedCallback;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment musicianFragment = getParentFragment();
        if (musicianFragment == null) {
            return;
        }

        Fragment roomFragment = musicianFragment.getParentFragment();
        if (roomFragment == null) {
            return;
        }

        onOwnSoundtrackChangedCallback = new ViewModelProvider(musicianFragment).get(MusicianViewViewModel.class);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        viewModel.observeAllSoundtracks(getViewLifecycleOwner());
        viewModel.observeCompositeSoundtrack(getViewLifecycleOwner());


        viewModel.getShowUploadDialog().observe(getViewLifecycleOwner(), showUploadDialog -> {
            if (showUploadDialog) {
                UiUtils.showInfoDialog(activity, getResources().getString(R.string.upload_help_title), getResources().getString(R.string.upload_help_message));
                viewModel.onUploadDialogShown();
            }
        });

        viewModel.getNetworkError().observe(getViewLifecycleOwner(), networkError -> {
            if (networkError != null) {
                UiUtils.showInfoDialog(activity, networkError.getTitle(), networkError.getMessage());
                viewModel.onNetworkErrorShown();
            }
        });

        return view;
    }
}
