package de.pcps.jamtugether.content.welcome;

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
import de.pcps.jamtugether.content.instrument.Instrument;
import de.pcps.jamtugether.content.instrument.InstrumentListAdapter;
import de.pcps.jamtugether.databinding.FragmentWelcomeBinding;

public class WelcomeFragment extends Fragment {

    @Inject
    NavigationManager navigationManager;

    private WelcomeViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInjector.inject(this);
        viewModel = new ViewModelProvider(this).get(WelcomeViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentWelcomeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_welcome, container, false);

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
