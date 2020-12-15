package de.pcps.jamtugether.ui.instrument;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import java.util.List;

import de.pcps.jamtugether.model.instrument.Instrument;
import de.pcps.jamtugether.model.instrument.Instruments;

public class SpinnerDataBindingUtils {

    @BindingAdapter(value = {"currentInstrument", "instrumentList", "clickListener"})
    public static void setInstruments(@NonNull Spinner spinner, @NonNull Instrument currentInstrument, @NonNull List<Instrument> instrumentList, @NonNull Instrument.ClickListener clickListener) {
        InstrumentSpinnerAdapter adapter = new InstrumentSpinnerAdapter(spinner.getContext(), instrumentList);
        spinner.setAdapter(adapter);
        spinner.setSelection(currentInstrument.getOrdinal());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Instrument mainInstrument = Instruments.LIST[position];
                clickListener.onInstrumentClicked(mainInstrument);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
}
