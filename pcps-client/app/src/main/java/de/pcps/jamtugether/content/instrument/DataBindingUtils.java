package de.pcps.jamtugether.content.instrument;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;

import java.util.List;

public class DataBindingUtils {

    @BindingAdapter(value = {"currentInstrument", "instrumentList", "clickListener"})
    public static void setInstruments(Spinner spinner, Instrument currentInstrument, List<Instrument> instrumentList, Instrument.ClickListener clickListener) {
        InstrumentSpinnerAdapter adapter = new InstrumentSpinnerAdapter(spinner.getContext(), instrumentList);
        spinner.setAdapter(adapter);
        spinner.setSelection(currentInstrument.ordinal());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Instrument mainInstrument = Instrument.values()[position];
                clickListener.onInstrumentClicked(mainInstrument);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
}
