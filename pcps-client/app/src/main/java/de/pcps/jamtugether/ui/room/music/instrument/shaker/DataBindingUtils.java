package de.pcps.jamtugether.ui.room.music.instrument.shaker;

import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.ui.room.music.instrument.shaker.view.ShakerView;

public class DataBindingUtils {

    @BindingAdapter(value = {"intensity", "viewModel"})
    public static void setIntensity(@NonNull ShakerView shakerView, float intensity, @NonNull ShakerViewModel shakerViewModel) {
        if (intensity > 0) {
            shakerView.startAnimation(AnimationUtils.loadAnimation(shakerView.getContext(), R.anim.shake));
            shakerViewModel.onShakeAnimationStarted();
        }
    }

    @BindingAdapter("soundtracksExpanded")
    public static void setSoundtracksExpanded(@NonNull ShakerView shakerView, boolean soundtracksExpanded) {
        shakerView.setSoundtracksExpanded(soundtracksExpanded);
    }
}
