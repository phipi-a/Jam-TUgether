package de.pcps.jamtugether.ui.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public abstract class BaseSpinnerAdapter<T> extends ArrayAdapter<T> {

    public BaseSpinnerAdapter(@NonNull Context context, @NonNull List<T> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent, View.TEXT_ALIGNMENT_TEXT_END);
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent, View.TEXT_ALIGNMENT_TEXT_START);
    }

    @NonNull
    protected abstract View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent, int textAlignment);
}
