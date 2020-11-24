package de.pcps.jamtugether.content.settings;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;

import de.pcps.jamtugether.content.instrument.Instrument;
import de.pcps.jamtugether.content.instrument.InstrumentSpinnerAdapter;

public class DataBindingUtils {

    @BindingAdapter(value = {"viewModel"})
    public static void setInstruments(Spinner spinner, SettingsViewModel viewModel) {
        InstrumentSpinnerAdapter adapter = new InstrumentSpinnerAdapter(spinner.getContext(), viewModel.getInstruments());
        spinner.setAdapter(adapter);
        spinner.setSelection(viewModel.getMainInstrument().ordinal());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Instrument mainInstrument = Instrument.values()[position];
                viewModel.onMainInstrumentSelected(mainInstrument);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
}
