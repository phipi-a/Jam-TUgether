package de.pcps.jamtugether.ui.onboarding;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.di.AppInjector;
import de.pcps.jamtugether.storage.Preferences;
import de.pcps.jamtugether.ui.base.views.indicator.PageIndicator;

public class OnBoardingViewModel extends ViewModel implements PageIndicator.OnClickListener {

    @Inject
    Preferences preferences;

    @NonNull
    private final MutableLiveData<Integer> pagePosition = new MutableLiveData<>(0);

    @NonNull
    private final MutableLiveData<List<PageIndicator>> pageIndicators = new MutableLiveData<>(PageIndicator.createList(OnBoardingAdapter.ON_BOARDING_ITEM_COUNT, 0));

    @NonNull
    private final MutableLiveData<Integer> backButtonVisibility = new MutableLiveData<>(View.INVISIBLE);

    @NonNull
    private final MutableLiveData<Integer> nextButtonVisibility = new MutableLiveData<>(View.VISIBLE);

    @NonNull
    private final MutableLiveData<Integer> finishButtonVisibility = new MutableLiveData<>(View.INVISIBLE);

    @NonNull
    private final MutableLiveData<Boolean> navigateToMenu = new MutableLiveData<>(false);

    @NonNull
    private final MutableLiveData<Boolean> navigateToSettings = new MutableLiveData<>(false);

    @NonNull
    private final ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            pagePosition.setValue(position);
            pageIndicators.setValue(PageIndicator.createList(OnBoardingAdapter.ON_BOARDING_ITEM_COUNT, position));

            if (position == 0) {
                backButtonVisibility.setValue(View.GONE);
            } else {
                backButtonVisibility.setValue(View.VISIBLE);
            }
            if (position == OnBoardingAdapter.ON_BOARDING_ITEM_COUNT - 1) {
                nextButtonVisibility.setValue(View.GONE);
                finishButtonVisibility.setValue(View.VISIBLE);
            } else {
                nextButtonVisibility.setValue(View.VISIBLE);
                finishButtonVisibility.setValue(View.GONE);
            }
        }
    };

    public OnBoardingViewModel() {
        AppInjector.inject(this);
    }

    @Override
    public void onClicked(@NonNull PageIndicator pageIndicator) {
        pagePosition.setValue(pageIndicator.getPosition());
    }

    public void onBackButtonClicked() {
        if (pagePosition.getValue() != null) {
            pagePosition.setValue(pagePosition.getValue() - 1);
        }
    }

    public void onNextButtonClicked() {
        if (pagePosition.getValue() != null) {
            pagePosition.setValue(pagePosition.getValue() + 1);
        }
    }

    public void onFinishButtonClicked() {
        if (preferences.userCompletedOnBoarding()) { // user watched on boarding again from settings
            navigateToSettings.setValue(true);
        } else { // user watched on boarding for the first time
            preferences.setUserCompletedOnBoarding(true);
            navigateToMenu.setValue(true);
        }
    }

    public void onNavigatedToMenu() {
        navigateToMenu.setValue(false);
    }

    public void onNavigatedToSettings() {
        navigateToSettings.setValue(false);
    }

    @NonNull
    public ViewPager2.OnPageChangeCallback getOnPageChangeCallback() {
        return onPageChangeCallback;
    }

    @NonNull
    public LiveData<Integer> getPagePosition() {
        return pagePosition;
    }

    @NonNull
    public LiveData<List<PageIndicator>> getPageIndicators() {
        return pageIndicators;
    }

    @NonNull
    public LiveData<Integer> getBackButtonVisibility() {
        return backButtonVisibility;
    }

    @NonNull
    public LiveData<Integer> getNextButtonVisibility() {
        return nextButtonVisibility;
    }

    @NonNull
    public LiveData<Integer> getFinishButtonVisibility() {
        return finishButtonVisibility;
    }

    @NonNull
    public LiveData<Boolean> getNavigateToMenu() {
        return navigateToMenu;
    }

    @NonNull
    public LiveData<Boolean> getNavigateToSettings() {
        return navigateToSettings;
    }
}
