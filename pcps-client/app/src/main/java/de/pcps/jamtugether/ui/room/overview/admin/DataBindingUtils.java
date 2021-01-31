package de.pcps.jamtugether.ui.room.overview.admin;

import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import java.util.List;

public class DataBindingUtils {

    @BindingAdapter({"items", "currentSelection"})
    public static void setItems(@NonNull Spinner spinner, List<Integer> items, int currentSelection) {
        spinner.setAdapter(new MetronomeSpinnerAdapter(spinner.getContext(), items));
        spinner.setSelection(currentSelection);
    }
}
