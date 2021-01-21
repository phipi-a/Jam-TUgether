package de.pcps.jamtugether.ui.instrument;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import java.util.List;

import de.pcps.jamtugether.audio.instrument.base.Instrument;

public class SpinnerDataBindingUtils {

    @BindingAdapter(value = {"currentInstrument", "instrumentList", "clickListener"})
    public static void setInstruments(@NonNull Spinner spinner, @NonNull Instrument currentInstrument, @NonNull List<Instrument> instrumentList, @NonNull Instrument.OnClickListener onClickListener) {
        InstrumentSpinnerAdapter adapter = new InstrumentSpinnerAdapter(spinner.getContext(), instrumentList);
        spinner.setAdapter(adapter);
        spinner.setSelection(currentInstrument.getOrdinal());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Instrument mainInstrument = instrumentList.get(position);
                onClickListener.onInstrumentClicked(mainInstrument);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
