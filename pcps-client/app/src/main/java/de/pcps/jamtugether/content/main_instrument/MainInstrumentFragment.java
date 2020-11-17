package de.pcps.jamtugether.content.main_instrument;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.Arrays;

import javax.inject.Inject;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.base.dagger.AppInjector;
import de.pcps.jamtugether.base.navigation.NavigationManager;
import de.pcps.jamtugether.content.main_instrument.instrument.Instrument;
import de.pcps.jamtugether.content.main_instrument.instrument.InstrumentListAdapter;
import de.pcps.jamtugether.databinding.FragmentMainInstrumentBinding;

public class MainInstrumentFragment extends Fragment {

    @Inject
    NavigationManager navigationManager;

    private MainInstrumentViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInjector.inject(this);

        viewModel = new ViewModelProvider(this).get(MainInstrumentViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentMainInstrumentBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_instrument, container, false);

        InstrumentListAdapter adapter = new InstrumentListAdapter(viewModel);
        binding.instrumentsRecyclerView.setAdapter(adapter);
        adapter.submitList(Arrays.asList(Instrument.values()));

        viewModel.getNavigateToMenu().observe(getViewLifecycleOwner(), navigateToMenu -> {
            if(navigateToMenu) {
                navigationManager.navigateToMenu(Navigation.findNavController(binding.getRoot()));
                viewModel.onNavigatedToMenu();
            }
        });

        return binding.getRoot();
    }
}
