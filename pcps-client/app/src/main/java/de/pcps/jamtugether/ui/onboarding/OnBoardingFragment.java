package de.pcps.jamtugether.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import de.pcps.jamtugether.databinding.FragmentOnBoardingBinding;
import de.pcps.jamtugether.ui.base.BaseFragment;
import de.pcps.jamtugether.ui.base.views.indicator.PageIndicatorAdapter;
import de.pcps.jamtugether.utils.NavigationUtils;

public class OnBoardingFragment extends BaseFragment {

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
        FragmentOnBoardingBinding binding = FragmentOnBoardingBinding.inflate(inflater, container, false);
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
                NavigationUtils.navigateToMenu(getNavController());
                viewModel.onNavigatedToMenu();
            }
        });

        viewModel.getNavigateToSettings().observe(getViewLifecycleOwner(), navigateToSettings -> {
            if (navigateToSettings) {
                NavigationUtils.navigateBack(getNavController());
                viewModel.onNavigatedToSettings();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewPager.unregisterOnPageChangeCallback(viewModel.getOnPageChangeCallback());
    }
}
