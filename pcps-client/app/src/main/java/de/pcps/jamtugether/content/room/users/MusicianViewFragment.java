package de.pcps.jamtugether.content.room.users;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.databinding.FragmentMusicianViewBinding;
import de.pcps.jamtugether.utils.UiUtils;

public class MusicianViewFragment extends Fragment {

    private MusicianViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MusicianViewModel.class);
    }

    @NonNull
    public static MusicianViewFragment newInstance() {
        return new MusicianViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMusicianViewBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_musician_view, container, false);
        binding.setViewModel(viewModel);

        viewModel.getShowHelpDialog().observe(getViewLifecycleOwner(), showHelpDialog -> {
            if (showHelpDialog) {
                AlertDialog dialog = UiUtils.createInfoDialog(getContext(), "test", "blow in your Instrument");
                dialog.setOnShowListener(arg -> viewModel.onHelpDialogShown());
            }
        });

        return binding.getRoot();
    }
}