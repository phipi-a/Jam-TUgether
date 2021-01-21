package de.pcps.jamtugether.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import de.pcps.jamtugether.databinding.FragmentOnboardingBinding;
import de.pcps.jamtugether.ui.base.views.PageIndicatorAdapter;
import de.pcps.jamtugether.utils.NavigationUtils;

public class OnBoardingFragment extends Fragment {

    private OnBoardingViewModel viewModel;

    private ViewPager2 viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(OnBoardingViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentOnboardingBinding binding = FragmentOnboardingBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        this.viewPager = binding.viewPager;
        viewPager.setAdapter(new OnBoardingAdapter(this));
        viewPager.registerOnPageChangeCallback(viewModel.getOnPageChangeCallback());

        PageIndicatorAdapter pageIndicatorAdapter = new PageIndicatorAdapter(viewModel);
        binding.pageIndicatorsView.setAdapter(pageIndicatorAdapter);
        viewModel.getPageIndicators().observe(getViewLifecycleOwner(), pageIndicatorAdapter::submitList);

        viewModel.getNavigateToMenu().observe(getViewLifecycleOwner(), navigateToMenu -> {
            if (navigateToMenu) {
                NavigationUtils.navigateToMenu(NavHostFragment.findNavController(this));
                viewModel.onNavigatedToMenu();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewPager.registerOnPageChangeCallback(viewModel.getOnPageChangeCallback());
    }
}
