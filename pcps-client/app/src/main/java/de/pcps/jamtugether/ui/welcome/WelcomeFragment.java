package de.pcps.jamtugether.ui.welcome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.utils.NavigationUtils;
import de.pcps.jamtugether.ui.instrument.InstrumentListAdapter;
import de.pcps.jamtugether.databinding.FragmentWelcomeBinding;

public class WelcomeFragment extends BaseFragment {

    private WelcomeViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(WelcomeViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentWelcomeBinding binding = FragmentWelcomeBinding.inflate(inflater, container, false);

        InstrumentListAdapter adapter = new InstrumentListAdapter(viewModel);
        binding.instrumentsRecyclerView.setAdapter(adapter);
        adapter.submitList(viewModel.getInstruments());

        viewModel.getNavigateToMenu().observe(getViewLifecycleOwner(), navigateToMenu -> {
            if(navigateToMenu) {
                NavigationUtils.navigateToMenu(NavHostFragment.findNavController(this));
                viewModel.onNavigatedToMenu();
            }
        });

        return binding.getRoot();
    }
}
