package de.pcps.jamtugether.ui.room.music.soundtrack;

import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import de.pcps.jamtugether.ui.room.music.instrument.InstrumentViewModel;

public class DataBindingUtils {

    @BindingAdapter("recordButtonImage")
    public static void setRecordButtonImage(@NonNull ImageView recordButton, @DrawableRes int recordButtonImage) {
        recordButton.setImageResource(recordButtonImage);
    }

    @BindingAdapter("recordButtonColor")
    public static void setRecordButtonColor(@NonNull ImageView recordButton, @ColorRes int recordButtonColor) {
        recordButton.setColorFilter(ContextCompat.getColor(recordButton.getContext(), recordButtonColor));
    }

    @BindingAdapter(value = {"uncheckLoop", "viewModel"})
    public static void uncheckLoopCheckBox(@NonNull CheckBox compositeSoundtrackLoopCheckBox, boolean uncheck, @NonNull InstrumentViewModel viewModel) {
        if (uncheck) {
            compositeSoundtrackLoopCheckBox.setChecked(false);
            viewModel.onLoopCheckBoxUnchecked();
        }
    }

    @BindingAdapter(value = {"uncheckCompositeSoundtrack", "viewModel"})
    public static void uncheckCompositeSoundtrackCheckBox(@NonNull CheckBox compositeSoundtrackCheckBox, boolean uncheck, @NonNull InstrumentViewModel viewModel) {
        if (uncheck) {
            compositeSoundtrackCheckBox.setChecked(false);
            viewModel.onCompositeSoundtrackCheckBoxUnchecked();
        }
    }
}
