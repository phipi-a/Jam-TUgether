package de.pcps.jamtugether.ui.base.views.indicator;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.ArrayList;
import java.util.List;

public class PageIndicator {

    public static final DiffUtil.ItemCallback<PageIndicator> DIFF_UTIL_CALLBACK = new DiffUtil.ItemCallback<PageIndicator>() {

        @Override
        public boolean areItemsTheSame(@NonNull PageIndicator oldItem, @NonNull PageIndicator newItem) {
            return oldItem.position == newItem.position;
        }

        @Override
        public boolean areContentsTheSame(@NonNull PageIndicator oldItem, @NonNull PageIndicator newItem) {
            return oldItem.active == newItem.active;
        }
    };

    private final int position;

    private final boolean active;

    public PageIndicator(int position, boolean active) {
        this.position = position;
        this.active = active;
    }

    public int getPosition() {
        return position;
    }

    public boolean isActive() {
        return active;
    }

    @NonNull
    public static List<PageIndicator> createList(int size, int activePosition) {
        List<PageIndicator> indicators = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            indicators.add(new PageIndicator(i, i == activePosition));
        }
        return indicators;
    }

    public interface OnClickListener {

        void onClicked(@NonNull PageIndicator pageIndicator);
    }
}
