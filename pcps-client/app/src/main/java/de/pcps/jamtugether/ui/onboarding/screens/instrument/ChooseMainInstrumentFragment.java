package de.pcps.jamtugether.ui.onboarding.screens.instrument;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.databinding.FragmentChooseMainInstrumentBinding;
import de.pcps.jamtugether.ui.base.BaseFragment;

public class ChooseMainInstrumentFragment extends BaseFragment {

    private ChooseMainInstrumentViewModel viewModel;

    @NonNull
    public static ChooseMainInstrumentFragment newInstance() {
        return new ChooseMainInstrumentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ChooseMainInstrumentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentChooseMainInstrumentBinding binding = FragmentChooseMainInstrumentBinding.inflate(inflater, container, false);
        binding.instrumentsRadioGroup.setup(viewModel.getInstruments(), viewModel, viewModel.getMainInstrument());

        return binding.getRoot();
    }
}
