package de.pcps.jamtugether.ui.room.music.instrument.flute;

import android.annotation.SuppressLint;
import android.graphics.drawable.ClipDrawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import de.pcps.jamtugether.ui.room.music.instrument.flute.view.FluteView;
import de.pcps.jamtugether.ui.room.music.instrument.shaker.view.ShakerView;

public class FluteDataBindingUtils {

    @SuppressLint("ClickableViewAccessibility")
    @BindingAdapter("touchListener")
    public static void setTouchListener(@NonNull View self, @NonNull FluteViewModel viewModel) {
        self.setOnTouchListener((view, event) -> {
            int[] location = new int[2];
            view.getLocationInWindow(location);
            float pitchPercentage = 1 - (event.getRawY() - location[1]) / view.getHeight();
            viewModel.onPitchPercentageChanged(pitchPercentage);
            return true;
        });
    }

    @BindingAdapter("pitchLevel")
    public static void setPitchPercentage(@NonNull ImageView fluteFillImageView, int pitchLevel) {
        ClipDrawable clipDrawable = (ClipDrawable) fluteFillImageView.getDrawable();
        clipDrawable.setLevel(pitchLevel);
    }

    @BindingAdapter("soundtracksExpanded")
    public static void setSoundtracksExpanded(@NonNull FluteView fluteView, boolean soundtracksExpanded) {
        fluteView.setSoundtracksExpanded(soundtracksExpanded);
    }
}
