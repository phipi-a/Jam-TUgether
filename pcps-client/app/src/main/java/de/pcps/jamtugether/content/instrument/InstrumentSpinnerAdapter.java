package de.pcps.jamtugether.content.instrument;

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

public class InstrumentSpinnerAdapter extends ArrayAdapter<Instrument> {

    public InstrumentSpinnerAdapter(@NonNull Context context, @NonNull List<Instrument> instruments) {
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
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.view_instrument_spinner_item, parent, false);
        }

        Instrument instrument = getItem(position);
        TextView instrumentTextView = convertView.findViewById(R.id.instrument_text_view);
        instrumentTextView.setTextAlignment(textAlignment);
        instrumentTextView.setText(instrument.getName());

        return convertView;
    }
}

