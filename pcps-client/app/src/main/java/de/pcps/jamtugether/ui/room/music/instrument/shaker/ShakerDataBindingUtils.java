package de.pcps.jamtugether.ui.room.music.instrument.shaker;

import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import de.pcps.jamtugether.R;

public class ShakerDataBindingUtils {

    @BindingAdapter(value = {"intensity", "viewModel"})
    public static void setIntensity(@NonNull ImageView shakerImageView, float intensity, @NonNull ShakerViewModel shakerViewModel) {
        if (intensity > 0) {
            shakerImageView.startAnimation(AnimationUtils.loadAnimation(shakerImageView.getContext(), R.anim.shake));
            shakerViewModel.onShakeAnimationStarted();
        }
    }
}
