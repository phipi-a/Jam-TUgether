package de.pcps.jamtugether.ui.room.music.metronome;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

public class MetronomeDataBindingUtils {

    @BindingAdapter("metronomePlaying")
    public static void setEnabled(@NonNull ImageView metronomeButton, boolean metronomePlaying) {
        metronomeButton.setEnabled(metronomePlaying);
    }
}
