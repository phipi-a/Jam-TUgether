package de.pcps.jamtugether.ui.onboarding;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class OnBoardingDataBindingUtils {

    @BindingAdapter(("page"))
    public static void setPage(@NonNull ViewPager2 viewPager, int position) {
        viewPager.setCurrentItem(position);
    }
}
