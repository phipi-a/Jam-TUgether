package de.pcps.jamtugether.ui.room.music.instrument.flute;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import de.pcps.jamtugether.model.sound.flute.FluteSound;

public class FluteDataBindingUtils {

    @BindingAdapter("onPitchChanged")
    public static void setOnChangeListener(@NonNull Slider slider, @NonNull Slider.OnChangeListener sliderOnChangeListener) {
        slider.addOnChangeListener(sliderOnChangeListener);
        slider.setLabelFormatter((LabelFormatter) value -> FluteSound.from((int) value).getLabel(slider.getContext()));
    }
}
