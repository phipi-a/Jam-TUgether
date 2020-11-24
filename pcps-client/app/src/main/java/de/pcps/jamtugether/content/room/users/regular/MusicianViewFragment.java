package de.pcps.jamtugether.content.room.users.regular;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import javax.inject.Inject;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.base.dagger.AppInjector;
import de.pcps.jamtugether.base.navigation.NavigationManager;
import de.pcps.jamtugether.databinding.FragmentMusicianViewBinding;

// todo
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

        viewModel.getShowHelpDialog().observe(getViewLifecycleOwner(), ShowHelpDialog -> {
            if(ShowHelpDialog) {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("Delete entry")
                        .setMessage("blow in your Instrument")


                        .setPositiveButton(android.R.string.ok, (dialog1, which) -> {

                        })

                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .create();

                dialog.setOnShowListener(arg0 -> {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryTextColor));
                    viewModel.onHelpDialogShown();
                });
                dialog.show();

            }

        });return binding.getRoot();}
}