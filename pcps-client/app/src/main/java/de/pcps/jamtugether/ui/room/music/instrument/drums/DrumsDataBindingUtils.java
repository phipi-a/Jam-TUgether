package de.pcps.jamtugether.ui.room.music.instrument.drums;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

public class DrumsDataBindingUtils {

    @SuppressLint("ClickableViewAccessibility")
    @BindingAdapter(value = {"elementTouchListener", "element"})
    public static void setTouchListener(@NonNull ImageButton drumsElementButton, @NonNull DrumsViewModel viewModel, int element) {
        drumsElementButton.setOnTouchListener((view, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                switch (element) {
                    case 0:
                        viewModel.onSnareClicked();
                        break;
                    case 1:
                        viewModel.onKickClicked();
                        break;
                    case 2:
                        viewModel.onHatClicked();
                        break;
                    case 3:
                        viewModel.onCymbalClicked();
                        break;
                }
                return true;
            }
            return false;
        });
    }
}
