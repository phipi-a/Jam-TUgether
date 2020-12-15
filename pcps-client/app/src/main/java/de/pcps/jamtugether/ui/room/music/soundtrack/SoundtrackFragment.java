package de.pcps.jamtugether.ui.room.music.soundtrack;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.ui.room.music.MusicianViewViewModel;
import de.pcps.jamtugether.ui.soundtrack.SoundtrackDataBindingUtils;
import de.pcps.jamtugether.databinding.FragmentSoundtrackBinding;
import de.pcps.jamtugether.model.instrument.base.Instrument;
import de.pcps.jamtugether.utils.UiUtils;
import timber.log.Timber;

public class SoundtrackFragment extends BaseFragment {

    private static final String ROOM_ID_KEY = "room_id_key";
    private static final String TOKEN_KEY = "token_key";

    private Context context;

    private SoundtrackViewModel viewModel;

    @NonNull
    public static SoundtrackFragment newInstance(int roomID, @NonNull String token) {
        SoundtrackFragment fragment = new SoundtrackFragment();
        Bundle args = new Bundle();
        args.putInt(ROOM_ID_KEY, roomID);
        args.putString(TOKEN_KEY, token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            int roomID = getArguments().getInt(ROOM_ID_KEY);
            String token = getArguments().getString(TOKEN_KEY);
            if(getParentFragment() != null) {
                Instrument.OnChangeCallback onChangeCallback = new ViewModelProvider(getParentFragment(), new MusicianViewViewModel.Factory(roomID, token)).get(MusicianViewViewModel.class);
                SoundtrackViewModel.Factory viewModelFactory = new SoundtrackViewModel.Factory(roomID, onChangeCallback);
                viewModel = new ViewModelProvider(this, viewModelFactory).get(SoundtrackViewModel.class);
            } else {
                Timber.e("parent fragment is null (shouldn't happen)");
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSoundtrackBinding binding = FragmentSoundtrackBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);

        SoundtrackDataBindingUtils.bindCompositeSoundtrack(binding.compositeSoundtrackLayout.soundtrackControlsLayout, viewModel.getCompositeSoundtrack(), viewModel, getViewLifecycleOwner());
        binding.compositeSoundtrackLayout.soundtrackView.observeCompositeSoundtrack(viewModel.getCompositeSoundtrack(), getViewLifecycleOwner());

        SoundtrackDataBindingUtils.bindSingleSoundtrack(binding.ownSoundtrackLayout.soundtrackControlsLayout, viewModel.getOwnSoundtrack(), viewModel, getViewLifecycleOwner());
        binding.ownSoundtrackLayout.soundtrackView.observeSingleSoundtrack(viewModel.getOwnSoundtrack(), getViewLifecycleOwner());

        viewModel.getShowHelpDialog().observe(getViewLifecycleOwner(), showHelpDialog -> {
            if(showHelpDialog) {
                UiUtils.showInfoDialog(context, viewModel.getHelpDialogTitle(), viewModel.getHelpDialogMessage());
                viewModel.onHelpDialogShown();
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
