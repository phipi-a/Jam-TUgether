package de.pcps.jamtugether.ui.soundtrack;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.google.android.material.slider.Slider;

import de.pcps.jamtugether.databinding.ViewSoundtrackControlsBinding;
import de.pcps.jamtugether.model.music.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.music.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.music.soundtrack.base.Soundtrack;

public class SoundtrackDataBindingUtils {

    @BindingAdapter("onVolumeChanged")
    public static void setOnChangeListener(@NonNull Slider slider, @NonNull Slider.OnChangeListener sliderOnChangeListener) {
        slider.addOnChangeListener(sliderOnChangeListener);
    }

    public static void bindSingleSoundtrack(@NonNull ViewSoundtrackControlsBinding binding, @NonNull LiveData<SingleSoundtrack> soundtrack, @NonNull Soundtrack.OnChangeListener onChangeListener, @NonNull LifecycleOwner lifecycleOwner) {
        soundtrack.observe(lifecycleOwner, track -> {
            binding.setSoundtrack(track);
            binding.setLifecycleOwner(lifecycleOwner);
            binding.setOnChangeListener(onChangeListener);
        });
    }

    public static void bindCompositeSoundtrack(@NonNull ViewSoundtrackControlsBinding binding, @NonNull LiveData<CompositeSoundtrack> soundtrack, @NonNull Soundtrack.OnChangeListener onChangeListener, @NonNull LifecycleOwner lifecycleOwner) {
        soundtrack.observe(lifecycleOwner, track -> {
            binding.setSoundtrack(track);
            binding.setLifecycleOwner(lifecycleOwner);
            binding.setOnChangeListener(onChangeListener);
        });
    }
}
