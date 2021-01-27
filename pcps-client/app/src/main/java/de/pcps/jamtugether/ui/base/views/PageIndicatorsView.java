package de.pcps.jamtugether.ui.base.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PageIndicatorsView extends RecyclerView {

    public PageIndicatorsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
    }
}
