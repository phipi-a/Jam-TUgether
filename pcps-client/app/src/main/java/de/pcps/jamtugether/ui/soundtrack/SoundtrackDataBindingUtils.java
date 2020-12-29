package de.pcps.jamtugether.ui.soundtrack;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.google.android.material.slider.Slider;

import de.pcps.jamtugether.databinding.ViewSoundtrackControlsBinding;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import timber.log.Timber;

public class SoundtrackDataBindingUtils {

    @BindingAdapter("onVolumeChanged")
    public static void setOnChangeListener(@NonNull Slider slider, @NonNull Slider.OnChangeListener sliderOnChangeListener) {
        slider.addOnChangeListener(sliderOnChangeListener);
    }

    public static void bindSingleSoundtrack(@NonNull ViewSoundtrackControlsBinding binding, @NonNull LiveData<SingleSoundtrack> singleSoundtrack, @NonNull Soundtrack.OnChangeCallback onChangeCallback, @NonNull LifecycleOwner lifecycleOwner) {
        singleSoundtrack.observe(lifecycleOwner, soundtrack -> {
            binding.setSoundtrack(soundtrack);
            binding.setOnChangeListener(onChangeCallback);
            binding.setLifecycleOwner(lifecycleOwner);
        });
    }

    public static void bindCompositeSoundtrack(@NonNull ViewSoundtrackControlsBinding binding, @NonNull LiveData<CompositeSoundtrack> compositeSoundtrack, @NonNull Soundtrack.OnChangeCallback onChangeCallback, @NonNull LifecycleOwner lifecycleOwner) {
        compositeSoundtrack.observe(lifecycleOwner, soundtrack -> {
            binding.setSoundtrack(soundtrack);
            binding.setOnChangeListener(onChangeCallback);
            binding.setLifecycleOwner(lifecycleOwner);
        });
    }
}
