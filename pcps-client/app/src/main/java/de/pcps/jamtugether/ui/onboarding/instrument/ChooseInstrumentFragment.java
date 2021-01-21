package de.pcps.jamtugether.ui.onboarding.instrument;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.pcps.jamtugether.databinding.FragmentChooseInstrumentBinding;
import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.ui.instrument.InstrumentListAdapter;

public class ChooseInstrumentFragment extends BaseFragment {

    private ChooseInstrumentViewModel viewModel;

    @NonNull
    public static ChooseInstrumentFragment newInstance() {
        return new ChooseInstrumentFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ChooseInstrumentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentChooseInstrumentBinding binding = FragmentChooseInstrumentBinding.inflate(inflater, container, false);

        InstrumentListAdapter adapter = new InstrumentListAdapter(viewModel);
        binding.instrumentsRecyclerView.setAdapter(adapter);
        adapter.submitList(viewModel.getInstruments());

        return binding.getRoot();
    }
}
