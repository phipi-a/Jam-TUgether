package de.pcps.jamtugether.base.tablayout;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class JamTabLayout extends TabLayout {

    private ViewPager2 viewPager;

    public JamTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private final ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            JamTabLayout tabLayout = JamTabLayout.this;
            for (int currentPosition = 0; currentPosition < tabLayout.getTabCount(); currentPosition++) {
                JamTabView tabView = (JamTabView) tabLayout.getTabAt(currentPosition).getCustomView();
                boolean tabViewIsSelected = position == currentPosition;
                if (tabViewIsSelected) {
                    tabView.activate();
                } else {
                    tabView.deactivate();
                }
            }
        }
    };

    public void setup(@NonNull ViewPager2 viewPager, @NonNull TabLayoutAdapter adapter) {
        this.viewPager = viewPager;
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(this, viewPager, (tab, position) -> {
            JamTabView tabView = adapter.getTabView(position);
            tabView.setTitle(adapter.getTabTitle(position));
            tab.setCustomView(tabView);
            if (position == this.getSelectedTabPosition()) {
                tabView.activate();
            } else {
                tabView.deactivate();
            }
        }).attach();

        viewPager.registerOnPageChangeCallback(onPageChangeCallback);
    }

    public void unregisterOnPageChangeCallback() {
        if (viewPager != null && onPageChangeCallback != null) {
            viewPager.unregisterOnPageChangeCallback(onPageChangeCallback);
        }
    }
}
