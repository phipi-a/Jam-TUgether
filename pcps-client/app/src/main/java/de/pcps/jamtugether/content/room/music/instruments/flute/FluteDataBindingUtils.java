package de.pcps.jamtugether.content.room.music.instruments.flute;
import android.annotation.SuppressLint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.databinding.BindingAdapter;

public class FluteDataBindingUtils {
    @SuppressLint("ClickableViewAccessibility")
    @BindingAdapter("touchListener")
    public static void setTouchListener(View self, FluteViewModel viewModel){
        self.setOnTouchListener((view, event) -> {
            float soundPitchPercentage;
            int[] location = new int[2];
            view.getLocationInWindow(location);
            soundPitchPercentage = (event.getRawY()-location[1])/view.getHeight();

            viewModel.onPitchChanged(soundPitchPercentage);
            return true;
        });
    }
}
