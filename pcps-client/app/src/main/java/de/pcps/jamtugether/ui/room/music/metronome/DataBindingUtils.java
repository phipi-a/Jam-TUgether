package de.pcps.jamtugether.ui.room.music.metronome;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

public class DataBindingUtils {

    @BindingAdapter("color")
    public static void setColor(@NonNull ImageView metronomeImageView, int color) {
        metronomeImageView.setColorFilter(color);
    }
}
