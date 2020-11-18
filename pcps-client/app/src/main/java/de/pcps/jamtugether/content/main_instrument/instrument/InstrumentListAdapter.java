package de.pcps.jamtugether.content.main_instrument.instrument;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.databinding.ViewInstrumentBinding;

public class InstrumentListAdapter extends ListAdapter<Instrument, InstrumentListAdapter.ViewHolder> {

    @NonNull
    private final Instrument.ClickListener clickListener;

    public InstrumentListAdapter(@NonNull Instrument.ClickListener clickListener) {
        super(Instrument.DIFF_UTIL_CALLBACK);
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), clickListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ViewInstrumentBinding binding;

        ViewHolder(@NonNull ViewInstrumentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull Instrument instrument, @NonNull Instrument.ClickListener clickListener) {
            binding.setInstrument(instrument);
            binding.setClickListener(clickListener);
            binding.executePendingBindings();
        }

        @NonNull
        static ViewHolder from(@NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ViewHolder(DataBindingUtil.inflate(inflater, R.layout.view_instrument, parent, false));
        }
    }
}


