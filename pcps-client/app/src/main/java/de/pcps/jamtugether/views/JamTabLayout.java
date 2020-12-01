package de.pcps.jamtugether.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import de.pcps.jamtugether.content.room.TabLayoutAdapter;

public class JamTabLayout extends TabLayout {

    private ViewPager2 viewPager;

    @NonNull
    private final ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            JamTabLayout tabLayout = JamTabLayout.this;
            for (int currentPosition = 0; currentPosition < tabLayout.getTabCount(); currentPosition++) {
                Tab tab = tabLayout.getTabAt(currentPosition);
                if(tab == null || tab.getCustomView() == null) {
                    continue;
                }
                JamTabView tabView = (JamTabView) tab.getCustomView();
                boolean tabViewIsSelected = position == currentPosition;
                if (tabViewIsSelected) {
                    tabView.activate();
                } else {
                    tabView.deactivate();
                }
            }
        }
    };

    public JamTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

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
        if (viewPager != null) {
            viewPager.unregisterOnPageChangeCallback(onPageChangeCallback);
        }
    }
}
