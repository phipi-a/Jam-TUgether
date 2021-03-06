package de.pcps.jamtugether.ui.soundtrack;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.google.android.material.slider.Slider;

import de.pcps.jamtugether.databinding.ViewSoundtrackBinding;
import de.pcps.jamtugether.databinding.ViewSoundtrackControlsBinding;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import de.pcps.jamtugether.ui.soundtrack.views.SoundtrackContainer;

public class SoundtrackDataBindingUtils {

    @BindingAdapter("onVolumeChanged")
    public static void setOnChangeListener(@NonNull Slider slider, @NonNull Slider.OnChangeListener sliderOnChangeListener) {
        slider.addOnChangeListener(sliderOnChangeListener);
    }

    public static void bindOwnSoundtrack(@NonNull ViewSoundtrackBinding binding, @NonNull LiveData<SingleSoundtrack> ownSoundtrack, @NonNull Soundtrack.OnChangeCallback onChangeCallback, @NonNull LifecycleOwner lifecycleOwner) {
        ViewSoundtrackControlsBinding controlsBinding = binding.soundtrackControlsLayout;
        SoundtrackContainer soundtrackContainer = binding.soundtrackContainer;

        ownSoundtrack.observe(lifecycleOwner, soundtrack -> {
            binding.setSoundtrack(soundtrack);
            controlsBinding.setSoundtrack(soundtrack);
            controlsBinding.setOnChangeListener(onChangeCallback);
            controlsBinding.setLifecycleOwner(lifecycleOwner);
        });
        soundtrackContainer.observeSingleSoundtrack(ownSoundtrack, lifecycleOwner);
    }

    public static void bindCompositeSoundtrack(@NonNull ViewSoundtrackBinding binding, @NonNull LiveData<CompositeSoundtrack> compositeSoundtrack, @NonNull Soundtrack.OnChangeCallback onChangeCallback, @NonNull LifecycleOwner lifecycleOwner) {
        ViewSoundtrackControlsBinding controlsBinding = binding.soundtrackControlsLayout;
        SoundtrackContainer soundtrackContainer = binding.soundtrackContainer;
        compositeSoundtrack.observe(lifecycleOwner, soundtrack -> {
            binding.setSoundtrack(soundtrack);
            controlsBinding.setSoundtrack(soundtrack);
            controlsBinding.setOnChangeListener(onChangeCallback);
            controlsBinding.setLifecycleOwner(lifecycleOwner);
        });
        soundtrackContainer.observeCompositeSoundtrack(compositeSoundtrack, lifecycleOwner);
    }
}
