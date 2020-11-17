package de.pcps.jamtugether.content.main_instrument.instrument;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.DiffUtil;

import de.pcps.jamtugether.R;

public enum Instrument {
    FLUTE(R.string.instrument_flute, "flute"),
    DRUMS(R.string.instrument_drums,"drums"),
    SHAKER(R.string.instrument_shaker,"shaker");

    @StringRes
    private final int name;

    @NonNull
    private final String preferenceValue;

    Instrument(@StringRes int name, @NonNull String preferenceValue) {
        this.name = name;
        this.preferenceValue = preferenceValue;
    }

    @StringRes
    public int getName() {
        return name;
    }

    @NonNull
    public String getPreferenceValue() {
        return preferenceValue;
    }

    @NonNull
    public static Instrument from(@NonNull String preferenceValue) {
        for(Instrument instrument : values()) {
            if(instrument.preferenceValue.equals(preferenceValue)) {
                return instrument;
            }
        }

        return FLUTE;
    }

    public static final DiffUtil.ItemCallback<Instrument> DIFF_UTIL_CALLBACK = new DiffUtil.ItemCallback<Instrument>() {

        @Override
        public boolean areItemsTheSame(@NonNull Instrument oldItem, @NonNull Instrument newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Instrument oldItem, @NonNull Instrument newItem) {
            return oldItem == newItem;
        }
    };

    public interface ClickListener {
        void onInstrumentClicked(@NonNull Instrument instrument);
    }
}
