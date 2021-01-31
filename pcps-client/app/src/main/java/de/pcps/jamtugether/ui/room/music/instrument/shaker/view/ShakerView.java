package de.pcps.jamtugether.ui.room.music.instrument.shaker.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.utils.UiUtils;

public class ShakerView extends AppCompatImageView {

    @NonNull
    private final ConstraintSet constraintSet;

    @NonNull
    private final ConstraintLayout.LayoutParams layoutParams;

    private boolean soundtracksExpanded;

    private boolean drawn;
    private boolean mustDraw;

    public ShakerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.constraintSet = new ConstraintSet();
        this.layoutParams = new ConstraintLayout.LayoutParams(0, 0);
    }

    public void setSoundtracksExpanded(boolean soundtracksExpanded) {
        this.soundtracksExpanded = soundtracksExpanded;
        mustDraw = true;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mustDraw && drawn) {
            return;
        }

        ConstraintLayout shakerFragmentView = (ConstraintLayout) this.getParent();
        View linearLayout = (View) shakerFragmentView.getParent().getParent();
        View parent = (View) linearLayout.getParent();

        int ownSoundtrackViewHeight = linearLayout.findViewById(R.id.own_soundtrack_fragment_container).getHeight();
        int ownSoundtrackControlsHeight = shakerFragmentView.findViewById(R.id.own_soundtrack_controls_layout).getHeight();
        int ownSoundtrackControlsMarginTop = UiUtils.getPixels(getContext(), R.dimen.own_soundtrack_controls_margin_top);
        int shakerViewMarginTop = UiUtils.getPixels(getContext(), R.dimen.shaker_view_margin_top);
        int shakerViewPaddingBottom = UiUtils.getPixels(getContext(), R.dimen.shaker_view_padding_bottom);

        int shakerViewDefaultHeight = UiUtils.getPixels(getContext(), R.dimen.shaker_view_default_height);
        int shakerViewHeight;

        if (soundtracksExpanded) {
            shakerViewHeight = shakerViewDefaultHeight;
        } else {
            int availableHeight = parent.getHeight() -
                    (ownSoundtrackViewHeight + ownSoundtrackControlsHeight + ownSoundtrackControlsMarginTop + shakerViewMarginTop + shakerViewPaddingBottom);

            shakerViewHeight = Math.max(availableHeight, shakerViewDefaultHeight);
        }

        layoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = shakerViewHeight;
        this.setLayoutParams(layoutParams);

        this.setPadding(0, 0, 0, shakerViewPaddingBottom);

        constraintSet.clone(shakerFragmentView);
        constraintSet.connect(R.id.shaker_image_view, ConstraintSet.START, R.id.shaker_fragment_layout, ConstraintSet.START);
        constraintSet.connect(R.id.shaker_image_view, ConstraintSet.END, R.id.shaker_fragment_layout, ConstraintSet.END);
        constraintSet.connect(R.id.shaker_image_view, ConstraintSet.TOP, R.id.own_soundtrack_controls_layout, ConstraintSet.BOTTOM, shakerViewMarginTop);
        constraintSet.applyTo(shakerFragmentView);

        drawn = true;
        mustDraw = false;
    }
}
