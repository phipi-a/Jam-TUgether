package de.pcps.jamtugether.ui.room.music.instrument.flute;

import android.annotation.SuppressLint;
import android.graphics.drawable.ClipDrawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import de.pcps.jamtugether.model.sound.flute.FluteSound;

public class FluteDataBindingUtils {

    @SuppressLint("ClickableViewAccessibility")
    @BindingAdapter("touchListener")
    public static void setTouchListener(@NonNull View self, @NonNull FluteViewModel viewModel) {
        self.setOnTouchListener((view, event) -> {
            // todo
            int[] location = new int[2];
            view.getLocationInWindow(location);
            int noteHeight = view.getHeight() / FluteSound.values().length;
            double y = event.getRawY() - location[1];
            int pitch = FluteSound.DEFAULT.getPitch();
            for (int i = 0; i < FluteSound.values().length; i++) {
                if (noteHeight * i >= y) {
                    pitch = FluteSound.values()[i].getPitch();
                }
            }
            viewModel.onPitchChanged(pitch);
            return true;
        });
    }

    @BindingAdapter("pitch")
    public static void setPitchPercentage(@NonNull ImageView fluteFillImageView, int pitch) {
        ClipDrawable clipDrawable = (ClipDrawable) fluteFillImageView.getDrawable();
        clipDrawable.setLevel(pitch * 100);
    }
}
