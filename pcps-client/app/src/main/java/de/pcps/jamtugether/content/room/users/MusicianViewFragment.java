package de.pcps.jamtugether.content.room.users;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.content.room.users.instruments.drums.DrumsFragment;
import de.pcps.jamtugether.content.room.users.instruments.flute.FluteFragment;
import de.pcps.jamtugether.content.room.users.instruments.shaker.ShakerFragment;
import de.pcps.jamtugether.databinding.FragmentMusicianViewBinding;
import de.pcps.jamtugether.utils.UiUtils;

public class MusicianViewFragment extends Fragment {

    private static final String ROOM_ID_KEY = "room_id_key";

    private Context context;

    private MusicianViewViewModel viewModel;

    @NonNull
    public static MusicianViewFragment newInstance(int roomID) {
        MusicianViewFragment fragment = new MusicianViewFragment();
        Bundle args = new Bundle();
        args.putInt(ROOM_ID_KEY, roomID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            int roomID = getArguments().getInt(ROOM_ID_KEY);
            MusicianViewViewModel.Factory viewModelFactory = new MusicianViewViewModel.Factory(roomID);
            viewModel = new ViewModelProvider(this, viewModelFactory).get(MusicianViewViewModel.class);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMusicianViewBinding binding = FragmentMusicianViewBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);

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

        viewModel.getShowFluteFragment().observe(getViewLifecycleOwner(), showFluteFragment -> {
            if(showFluteFragment) {
                replaceInstrumentFragment(new FluteFragment());
                viewModel.onFluteFragmentShown();
            }
        });
        viewModel.getShowDrumsFragment().observe(getViewLifecycleOwner(), showDrumsFragment -> {
            if(showDrumsFragment) {
                replaceInstrumentFragment(new DrumsFragment());
                viewModel.onDrumsFragmentShown();
            }
        });
        viewModel.getShowShakerFragment().observe(getViewLifecycleOwner(), showShakerFragment -> {
            if(showShakerFragment) {
                replaceInstrumentFragment(new ShakerFragment());
                viewModel.onShakerFragmentShown();
            }
        });

        return binding.getRoot();
    }

    public void replaceInstrumentFragment(Fragment someFragment) {
                FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.instrument_container_fragment, someFragment);
                transaction.commit();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}