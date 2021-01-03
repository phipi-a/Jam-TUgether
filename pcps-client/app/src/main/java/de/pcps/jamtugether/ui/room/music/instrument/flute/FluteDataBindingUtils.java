package de.pcps.jamtugether.ui.room.music.instrument.flute;

import android.annotation.SuppressLint;
import android.graphics.drawable.ClipDrawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

public class FluteDataBindingUtils {

    @SuppressLint("ClickableViewAccessibility")
    @BindingAdapter("touchListener")
    public static void setTouchListener(@NonNull View self, @NonNull FluteViewModel viewModel) {
        self.setOnTouchListener((view, event) -> {
            float soundPitchPercentage;
            int[] location = new int[2];
            view.getLocationInWindow(location);
            soundPitchPercentage = (event.getRawY() - location[1]) / view.getHeight();
            viewModel.onPitchChanged(soundPitchPercentage);
            return true;
        });
    }

    @BindingAdapter("pitchPercentage")
    public static void setPitchPercentage(@NonNull ImageView fluteFillImageView, float pitchPercentage) {
        ClipDrawable clipDrawable = (ClipDrawable) fluteFillImageView.getDrawable();
        clipDrawable.setLevel((int) (10000 * pitchPercentage));
    }
}
