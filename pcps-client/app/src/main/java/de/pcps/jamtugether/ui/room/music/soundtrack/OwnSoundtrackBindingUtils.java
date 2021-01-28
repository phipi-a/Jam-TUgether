package de.pcps.jamtugether.ui.room.music.soundtrack;

import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentViewModel;

public class OwnSoundtrackBindingUtils {

    @BindingAdapter("startedCreatingSoundtrack")
    public static void setStartedCreatingSoundtrack(@NonNull ImageView playPauseButton, boolean startedCreatingSoundtrack) {
        int imageRes = startedCreatingSoundtrack ? R.drawable.ic_stop : R.drawable.ic_record;
        int color = ContextCompat.getColor(playPauseButton.getContext(), startedCreatingSoundtrack ? R.color.iconColor : R.color.recordButtonColor);
        playPauseButton.setImageResource(imageRes);
        playPauseButton.setColorFilter(color);
    }

    @BindingAdapter(value = {"uncheck", "viewModel"})
    public static void uncheckLoopCheckBox(@NonNull CheckBox compositeSoundtrackLoopCheckBox, boolean uncheck, @NonNull InstrumentViewModel viewModel) {
        if (uncheck) {
            compositeSoundtrackLoopCheckBox.setChecked(false);
            viewModel.onLoopCheckBoxUnchecked();
        }
    }
}
