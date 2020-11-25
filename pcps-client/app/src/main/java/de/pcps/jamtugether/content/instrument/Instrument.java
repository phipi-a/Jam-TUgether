package de.pcps.jamtugether.content.instrument;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.DiffUtil;

import java.util.HashMap;

import de.pcps.jamtugether.R;

public enum Instrument {
    FLUTE(R.string.instrument_flute, "flute", R.string.play_flute_help),
    DRUMS(R.string.instrument_drums,"drums", R.string.play_drums_help),
    SHAKER(R.string.instrument_shaker,"shaker", R.string.play_shaker_help);

    public static final Instrument FALLBACK = Instrument.FLUTE;

    @NonNull
    private static final HashMap<String, Instrument> preferenceMap = new HashMap<>();

    static {
        for(Instrument instrument : Instrument.values()) {
            preferenceMap.put(instrument.preferenceValue, instrument);
        }
    }

    @StringRes
    private final int name;

    @NonNull
    private final String preferenceValue;

    @StringRes
    private final int helpMessage;

    Instrument(@StringRes int name, @NonNull String preferenceValue, @StringRes int helpMessage) {
        this.name = name;
        this.preferenceValue = preferenceValue;
        this.helpMessage = helpMessage;
    }

    @StringRes
    public int getName() {
        return name;
    }

    @NonNull
    public String getPreferenceValue() {
        return preferenceValue;
    }

    @StringRes
    public int getHelpMessage() {
        return helpMessage;
    }

    @NonNull
    public static Instrument from(@NonNull String preferenceValue) {
        Instrument instrument = preferenceMap.get(preferenceValue);
        return instrument != null ? instrument : FALLBACK;
    }

    @NonNull
    public static final DiffUtil.ItemCallback<Instrument> DIFF_UTIL_CALLBACK = new DiffUtil.ItemCallback<Instrument>() {

        @Override
        public boolean areItemsTheSame(@NonNull Instrument oldItem, @NonNull Instrument newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Instrument oldItem, @NonNull Instrument newItem) {
            return oldItem.equals(newItem);
        }
    };

    public interface ClickListener {
        void onInstrumentClicked(@NonNull Instrument instrument);
    }
}
