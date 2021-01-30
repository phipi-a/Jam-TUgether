package de.pcps.jamtugether.ui.room.overview;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import de.pcps.jamtugether.R;

public class DataBindingUtils {

    @BindingAdapter("isAdmin")
    public static void setAdminIconColor(@NonNull ImageView imageView, boolean isAdmin) {
        int color = ContextCompat.getColor(imageView.getContext(), isAdmin ? R.color.adminIconActiveColor : R.color.adminIconInactiveColor);
        imageView.setColorFilter(color);
    }
}
