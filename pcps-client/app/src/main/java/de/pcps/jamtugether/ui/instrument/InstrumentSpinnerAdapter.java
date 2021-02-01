package de.pcps.jamtugether.ui.instrument;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.ui.base.BaseSpinnerAdapter;

public class InstrumentSpinnerAdapter extends BaseSpinnerAdapter<Instrument> {

    public InstrumentSpinnerAdapter(@NonNull Context context, @NonNull List<Instrument> instruments) {
        super(context, instruments);
    }

    @NonNull
    @Override
    protected View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent, int textAlignment) {
        if (convertView == null) {
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

