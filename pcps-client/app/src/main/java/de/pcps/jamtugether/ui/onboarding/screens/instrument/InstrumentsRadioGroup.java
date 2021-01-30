package de.pcps.jamtugether.ui.onboarding.screens.instrument;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import java.util.List;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.databinding.ViewInstrumentRadioButtonBinding;

public class InstrumentsRadioGroup extends RadioGroup {

    public InstrumentsRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public void setup(@NonNull List<Instrument> instruments, @NonNull Instrument.OnSelectionListener onSelectionListener, @NonNull Instrument mainInstrument) {
        this.removeAllViews();
        for (Instrument instrument : instruments) {
            RadioButton view = createView(instrument, onSelectionListener);
            this.addView(view);
            view.setChecked(instrument == mainInstrument);
        }
    }

    @NonNull
    private RadioButton createView(@NonNull Instrument instrument, @NonNull Instrument.OnSelectionListener onSelectionListener) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        ViewInstrumentRadioButtonBinding binding = ViewInstrumentRadioButtonBinding.inflate(layoutInflater, this, false);
        binding.setInstrument(instrument);
        binding.setOnSelectionListener(onSelectionListener);
        return (RadioButton) binding.getRoot();
    }
}
