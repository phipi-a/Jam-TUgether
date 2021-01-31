package de.pcps.jamtugether.ui.room.music.soundtrack.metronome;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

public class MetronomeDataBindingUtils {

    @BindingAdapter("color")
    public static void setColor(@NonNull ImageView metronomeImageView, int color) {
        metronomeImageView.setColorFilter(color);
    }
}
