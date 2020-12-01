package de.pcps.jamtugether.content.soundtrack;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.google.android.material.slider.Slider;

public class DataBindingUtil {

    @BindingAdapter("onVolumeChanged")
    public static void setOnChangeListener(@NonNull Slider slider, @NonNull Slider.OnChangeListener sliderOnChangeListener) {
        slider.addOnChangeListener(sliderOnChangeListener);
    }
}
