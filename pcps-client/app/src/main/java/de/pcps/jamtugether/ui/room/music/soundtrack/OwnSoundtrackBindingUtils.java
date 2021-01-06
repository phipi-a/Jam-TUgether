package de.pcps.jamtugether.ui.room.music.soundtrack;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import de.pcps.jamtugether.R;

public class OwnSoundtrackBindingUtils {

    @BindingAdapter("startedCreatingSoundtrack")
    public static void setStartedCreatingSoundtrack(@NonNull ImageView playPauseButton, boolean startedCreatingSoundtrack) {
        int imageRes = startedCreatingSoundtrack ? R.drawable.ic_stop : R.drawable.ic_record;
        int color = ContextCompat.getColor(playPauseButton.getContext(), startedCreatingSoundtrack ? R.color.iconColor : R.color.red);
        playPauseButton.setImageResource(imageRes);
        playPauseButton.setColorFilter(color);
    }
}
