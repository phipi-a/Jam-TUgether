package de.pcps.jamtugether.ui.onboarding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import de.pcps.jamtugether.ui.onboarding.instrument.ChooseInstrumentFragment;

public class OnBoardingAdapter extends FragmentStateAdapter {

    public static final int ON_BOARDING_ITEMS = 3;

    public OnBoardingAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // todo add fragments
        return ChooseInstrumentFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return ON_BOARDING_ITEMS;
    }
}
