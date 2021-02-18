package de.pcps.jamtugether.ui.onboarding;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import de.pcps.jamtugether.ui.onboarding.items.OnBoardingDrumsFragment;
import de.pcps.jamtugether.ui.onboarding.items.OnBoardingOverviewViewFragment;
import de.pcps.jamtugether.ui.onboarding.items.OnBoardingPianoFragment;
import de.pcps.jamtugether.ui.onboarding.items.OnBoardingReadyToUploadFragment;
import de.pcps.jamtugether.ui.onboarding.items.OnBoardingShakerFragment;
import de.pcps.jamtugether.ui.onboarding.items.ChooseMainInstrumentFragment;
import de.pcps.jamtugether.ui.onboarding.items.OnBoardingFluteFragment;
import de.pcps.jamtugether.ui.onboarding.items.OnBoardingMusicianViewFragment;
import de.pcps.jamtugether.ui.onboarding.items.OnBoardingRoomFragment;
import de.pcps.jamtugether.ui.onboarding.items.OnBoardingWelcomeFragment;

public class OnBoardingAdapter extends FragmentStateAdapter {

    public static final int ON_BOARDING_ITEM_COUNT = 10;

    static final List<Fragment> fragmentPositions = new ArrayList<Fragment>() {{
        add(OnBoardingWelcomeFragment.newInstance());
        add(OnBoardingRoomFragment.newInstance());
        add(OnBoardingMusicianViewFragment.newInstance());
        add(OnBoardingFluteFragment.newInstance());
        add(OnBoardingDrumsFragment.newInstance());
        add(OnBoardingShakerFragment.newInstance());
        add(OnBoardingPianoFragment.newInstance());
        add(OnBoardingReadyToUploadFragment.newInstance());
        add(OnBoardingOverviewViewFragment.newInstance());
        add(ChooseMainInstrumentFragment.newInstance());
    }};

    public OnBoardingAdapter(@NonNull OnBoardingFragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position < fragmentPositions.size()) {
            return fragmentPositions.get(position);
        }
        return OnBoardingWelcomeFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return ON_BOARDING_ITEM_COUNT;
    }
}
