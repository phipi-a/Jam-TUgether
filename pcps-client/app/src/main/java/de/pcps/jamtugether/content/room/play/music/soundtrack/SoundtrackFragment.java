package de.pcps.jamtugether.content.room.play.music.soundtrack;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.content.instrument.Instrument;
import de.pcps.jamtugether.databinding.FragmentSoundtrackBinding;
import de.pcps.jamtugether.utils.UiUtils;

public class SoundtrackFragment extends Fragment {

    private static final String ROOM_ID_KEY = "room_id_key";
    private static final String INSTRUMENT_ON_CHANGE_CALLBACK_KEY = "instrument_on_change_callback_key";

    private Context context;

    private SoundtrackViewModel viewModel;

    @NonNull
    public static SoundtrackFragment newInstance(int roomID, @NonNull Instrument.OnChangeCallback onChangeCallback) {
        SoundtrackFragment fragment = new SoundtrackFragment();
        Bundle args = new Bundle();
        args.putInt(ROOM_ID_KEY, roomID);
        args.putSerializable(INSTRUMENT_ON_CHANGE_CALLBACK_KEY, onChangeCallback);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            int roomID = getArguments().getInt(ROOM_ID_KEY);
            Instrument.OnChangeCallback onChangeCallback = (Instrument.OnChangeCallback) getArguments().getSerializable(INSTRUMENT_ON_CHANGE_CALLBACK_KEY);
            SoundtrackViewModel.Factory viewModelFactory = new SoundtrackViewModel.Factory(roomID, onChangeCallback);
            viewModel = new ViewModelProvider(this, viewModelFactory).get(SoundtrackViewModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSoundtrackBinding binding = FragmentSoundtrackBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);

        // todo add two sound track views and bind them to layout (like RoomOverviewFragment)

        viewModel.getShowHelpDialog().observe(getViewLifecycleOwner(), showHelpDialog -> {
            if(showHelpDialog) {
                AlertDialog dialog = UiUtils.createInfoDialog(context, viewModel.getHelpDialogTitle(), viewModel.getHelpDialogMessage());
                dialog.setOnShowListener(arg -> {
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(dialog.getContext(), R.color.primaryTextColor));
                    viewModel.onHelpDialogShown();
                });
                dialog.show();
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
