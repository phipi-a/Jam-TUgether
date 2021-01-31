package de.pcps.jamtugether.ui.room.overview.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.ui.base.BaseSpinnerAdapter;

public class MetronomeSpinnerAdapter extends BaseSpinnerAdapter<Integer> {

    public MetronomeSpinnerAdapter(@NonNull Context context, @NonNull List<Integer> instruments) {
        super(context, instruments);
    }

    @NonNull
    @Override
    protected View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent, int textAlignment) {
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