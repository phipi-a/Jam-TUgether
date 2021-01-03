package de.pcps.jamtugether.ui.room.music.instrument.drums.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.utils.UiUtils;

public class DrumsView extends LinearLayout {

    private ImageButton hatButton;
    private ImageButton kickButton;
    private ImageButton snareButton;
    private ImageButton cymbalButton;

    public DrumsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        this.hatButton = findViewById(R.id.hat_button);
        this.kickButton = findViewById(R.id.kick_button);
        this.snareButton = findViewById(R.id.snare_button);
        this.cymbalButton = findViewById(R.id.cymbal_button);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        View view = (View) this.getParent();
        int parentWidth = view.getWidth();

        int elementMargin = UiUtils.getPixels(getContext(), R.dimen.drums_element_margin);
        int elementPadding = UiUtils.getPixels(getContext(), R.dimen.drums_element_padding);
        int maxElementWidth = (int) ((parentWidth - elementMargin * 6 - elementPadding * 2) / 4.0);

        int defaultElementWidth = UiUtils.getPixels(getContext(), R.dimen.drums_element_width_default);
        int elementWidth = Math.min(maxElementWidth, defaultElementWidth);

        setSize(hatButton, elementWidth, 0, elementMargin);
        setSize(kickButton, elementWidth, elementMargin, elementMargin);
        setSize(snareButton, elementWidth, elementMargin, elementMargin);
        setSize(cymbalButton, elementWidth, elementMargin, 0);
    }

    private void setSize(@NonNull ImageButton elementButton, int width, int marginLeft, int marginRight) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, width);
        layoutParams.setMargins(marginLeft, 0, marginRight, 0);
        elementButton.setLayoutParams(layoutParams);
    }
}
