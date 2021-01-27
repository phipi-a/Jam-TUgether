package de.pcps.jamtugether.ui.soundtrack.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.material.slider.Slider;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.utils.UiUtils;

public class SoundtrackControlsView extends ConstraintLayout {

    private Slider volumeSlider;

    @NonNull
    private final ConstraintSet constraintSet;

    public SoundtrackControlsView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.constraintSet = new ConstraintSet();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.volumeSlider = findViewById(R.id.volume_slider);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        // all values necessary for calculating the available width of the volume slider
        int muteIconWidth = UiUtils.getPixels(getContext(), R.dimen.soundtrack_mute_icon_width);
        int volumeSliderMarginStart = UiUtils.getPixels(getContext(), R.dimen.soundtrack_volume_slider_margin_start);
        int volumeUpIconMarginStart = UiUtils.getPixels(getContext(), R.dimen.soundtrack_volume_up_icon_margin_start);
        int volumeUpIconWidth = UiUtils.getPixels(getContext(), R.dimen.soundtrack_volume_up_icon_width);
        int fastRewindIconMarginStart = UiUtils.getPixels(getContext(), R.dimen.soundtrack_fast_rewind_icon_margin_start);
        int fastRewindIconWidth = UiUtils.getPixels(getContext(), R.dimen.soundtrack_fast_rewind_icon_width);
        int playPauseIconMarginStart = UiUtils.getPixels(getContext(), R.dimen.soundtrack_play_pause_icon_margin_start);
        int playPauseIconWidth = UiUtils.getPixels(getContext(), R.dimen.soundtrack_play_pause_icon_width);
        int fastForwardIconMarginStart = UiUtils.getPixels(getContext(), R.dimen.soundtrack_fast_forward_icon_margin_start);
        int fastForwardIconWidth = UiUtils.getPixels(getContext(), R.dimen.soundtrack_fast_forward_icon_width);
        int stopIconMarginStart = UiUtils.getPixels(getContext(), R.dimen.soundtrack_stop_icon_margin_start);
        int stopIconWidth = UiUtils.getPixels(getContext(), R.dimen.soundtrack_stop_icon_width);
        int stopIconMarginEnd = UiUtils.getPixels(getContext(), R.dimen.soundtrack_stop_icon_margin_end);

        int availableWidthSlider =
                this.getWidth() - (
                        muteIconWidth + volumeSliderMarginStart + volumeUpIconMarginStart + volumeUpIconWidth +
                                fastRewindIconMarginStart + fastRewindIconWidth + playPauseIconMarginStart +
                                playPauseIconWidth + fastForwardIconMarginStart + fastForwardIconWidth +
                                stopIconMarginStart + stopIconWidth + stopIconMarginEnd
                );

        int maxSliderWidth = UiUtils.getPixels(getContext(), R.dimen.volume_slider_max_width);
        int sliderWidth = Math.min(availableWidthSlider, maxSliderWidth);

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(sliderWidth, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        volumeSlider.setLayoutParams(layoutParams);

        // update constraints
        constraintSet.clone(this);
        constraintSet.connect(R.id.volume_slider, ConstraintSet.START, R.id.volume_mute_icon, ConstraintSet.END, volumeSliderMarginStart);
        constraintSet.applyTo(this);
    }
}
