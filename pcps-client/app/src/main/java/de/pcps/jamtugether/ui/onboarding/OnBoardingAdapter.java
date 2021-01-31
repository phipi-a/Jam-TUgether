package de.pcps.jamtugether.ui.onboarding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import de.pcps.jamtugether.ui.onboarding.screens.OnBoardingDrumsFragment;
import de.pcps.jamtugether.ui.onboarding.screens.OnBoardingOverviewViewFragment;
import de.pcps.jamtugether.ui.onboarding.screens.OnBoardingPianoFragment;
import de.pcps.jamtugether.ui.onboarding.screens.OnBoardingReadyToUploadFragment;
import de.pcps.jamtugether.ui.onboarding.screens.OnBoardingShakerFragment;
import de.pcps.jamtugether.ui.onboarding.screens.instrument.ChooseMainInstrumentFragment;
import de.pcps.jamtugether.ui.onboarding.screens.OnBoardingFluteFragment;
import de.pcps.jamtugether.ui.onboarding.screens.OnBoardingMusicianViewFragment;
import de.pcps.jamtugether.ui.onboarding.screens.OnBoardingRoomFragment;
import de.pcps.jamtugether.ui.onboarding.screens.OnBoardingWelcomeFragment;

public class OnBoardingAdapter extends FragmentStateAdapter {

    public static final int ON_BOARDING_ITEM_COUNT = 10;

    public OnBoardingAdapter(@NonNull OnBoardingFragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return OnBoardingWelcomeFragment.newInstance();
            case 1:
                return OnBoardingRoomFragment.newInstance();
            case 2:
                return OnBoardingMusicianViewFragment.newInstance();
            case 3:
                return OnBoardingFluteFragment.newInstance();
            case 4:
                return OnBoardingDrumsFragment.newInstance();
            case 5:
                return OnBoardingShakerFragment.newInstance();
            case 6:
                return OnBoardingPianoFragment.newInstance();
            case 7:
                return OnBoardingReadyToUploadFragment.newInstance();
            case 8:
                return OnBoardingOverviewViewFragment.newInstance();
            case 9:
                return ChooseMainInstrumentFragment.newInstance();
        }
        return OnBoardingWelcomeFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return ON_BOARDING_ITEM_COUNT;
    }
}
