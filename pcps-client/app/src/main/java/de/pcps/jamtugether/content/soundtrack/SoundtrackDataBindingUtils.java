package de.pcps.jamtugether.content.soundtrack;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.google.android.material.slider.Slider;

import de.pcps.jamtugether.databinding.ViewSoundtrackControlsBinding;

public class SoundtrackDataBindingUtils {

    @BindingAdapter("onVolumeChanged")
    public static void setOnChangeListener(@NonNull Slider slider, @NonNull Slider.OnChangeListener sliderOnChangeListener) {
        slider.addOnChangeListener(sliderOnChangeListener);
    }

    /*
     * binds soundtrack live data and some other data to soundtrack controls layout
     */
    public static void bind(@NonNull ViewSoundtrackControlsBinding binding, @NonNull LiveData<Soundtrack> soundtrack, @NonNull Soundtrack.OnChangeListener onChangeListener, @NonNull LifecycleOwner lifecycleOwner) {
        soundtrack.observe(lifecycleOwner, track -> {
            binding.setSoundtrack(track);
            binding.setLifecycleOwner(lifecycleOwner);
            binding.setOnChangeListener(onChangeListener);
        });
    }
}
