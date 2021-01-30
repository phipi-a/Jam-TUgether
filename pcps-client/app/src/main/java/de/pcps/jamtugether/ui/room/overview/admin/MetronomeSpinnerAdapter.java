package de.pcps.jamtugether.ui.room.overview.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import de.pcps.jamtugether.R;

public class MetronomeSpinnerAdapter extends ArrayAdapter<Integer> {

    public MetronomeSpinnerAdapter(@NonNull Context context, @NonNull List<Integer> instruments) {
        super(context, 0, instruments);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getInstrumentView(position, convertView, parent, View.TEXT_ALIGNMENT_TEXT_END);
    }

    @NonNull
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getInstrumentView(position, convertView, parent, View.TEXT_ALIGNMENT_TEXT_START);
    }

    @NonNull
    private View getInstrumentView(int position, @Nullable View convertView, @NonNull ViewGroup parent, int textAlignment) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.view_metronom_spinner_item, parent, false);
        }

        Integer attribute = getItem(position);
        TextView instrumentTextView = convertView.findViewById(R.id.metronome_attribute_text_view);
        instrumentTextView.setTextAlignment(textAlignment);
        instrumentTextView.setText(String.valueOf(attribute));

        return convertView;
    }
}