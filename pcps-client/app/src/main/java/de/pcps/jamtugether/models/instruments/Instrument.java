package de.pcps.jamtugether.models.instruments;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.DiffUtil;

import java.io.Serializable;

public abstract class Instrument {

    private final int ordinal;

    @StringRes
    private final int name;

    @StringRes
    private final int helpMessage;

    @NonNull
    private final String preferenceValue;

    @NonNull
    private final String serverString;

    public Instrument(int ordinal, @StringRes int name, @StringRes int helpMessage, @NonNull String preferenceValue, @NonNull String serverString) {
        this.ordinal = ordinal;
        this.name = name;
        this.helpMessage = helpMessage;
        this.preferenceValue = preferenceValue;
        this.serverString = serverString;
    }

    public int getOrdinal() {
        return ordinal;
    }

    @StringRes
    public int getName() {
        return name;
    }

    @StringRes
    public int getHelpMessage() {
        return helpMessage;
    }

    @NonNull
    public String getPreferenceValue() {
        return preferenceValue;
    }

    @NonNull
    public String getServerString() {
        return serverString;
    }


    @NonNull
    public static final DiffUtil.ItemCallback<Instrument> DIFF_UTIL_CALLBACK = new DiffUtil.ItemCallback<Instrument>() {

        @Override
        public boolean areItemsTheSame(@NonNull Instrument oldItem, @NonNull Instrument newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Instrument oldItem, @NonNull Instrument newItem) {
            return true;
        }
    };

    public interface ClickListener {
        void onInstrumentClicked(@NonNull Instrument instrument);
    }

    public interface OnChangeCallback extends Serializable {
        void onInstrumentChanged(@NonNull Instrument instrument);
    }
}
