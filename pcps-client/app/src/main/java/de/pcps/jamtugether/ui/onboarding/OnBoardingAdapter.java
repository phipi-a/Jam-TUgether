package de.pcps.jamtugether.ui.onboarding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import de.pcps.jamtugether.ui.onboarding.instrument.ChooseMainInstrumentFragment;

public class OnBoardingAdapter extends FragmentStateAdapter {

    public static final int ON_BOARDING_ITEM_COUNT = 1;

    public OnBoardingAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // todo add fragments
        return ChooseMainInstrumentFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return ON_BOARDING_ITEM_COUNT;
    }
}
