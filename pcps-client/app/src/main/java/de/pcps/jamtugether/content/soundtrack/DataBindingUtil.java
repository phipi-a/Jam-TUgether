package de.pcps.jamtugether.content.soundtrack;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.google.android.material.slider.Slider;

public class DataBindingUtil {

    @BindingAdapter({"volumeListener", "soundtrack"})
    public static void setVolumeListener(@NonNull Slider slider, @NonNull Soundtrack.OnChangeListener onChangeListener, @NonNull Soundtrack soundtrack) {
        slider.addOnChangeListener((slider1, value, fromUser) -> onChangeListener.onVolumeChanged(soundtrack, value));
    }
}
