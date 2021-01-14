package de.pcps.jamtugether.ui.room.overview;

import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

public class DataBindingUtils {

    @BindingAdapter(value = {"isFetchingComposition", "viewModel"})
    public static void setVisibility(@NonNull ProgressBar progressBar, boolean isFetchingComposition, @NonNull SoundtrackOverviewViewModel viewModel) {
        if (viewModel.getLoadingOfCompositionShown()) {
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            if (isFetchingComposition) {
                progressBar.setVisibility(View.VISIBLE);
                viewModel.onLoadingOfCompositionShown();
            } else {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }
    }
}
