package de.pcps.jamtugether.ui.base.views;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import de.pcps.jamtugether.databinding.ViewPageIndicatorBinding;

public class PageIndicatorAdapter extends ListAdapter<PageIndicator, PageIndicatorAdapter.ViewHolder> {

    @NonNull
    private final PageIndicator.OnClickListener onClickListener;

    public PageIndicatorAdapter(@NonNull PageIndicator.OnClickListener onClickListener) {
        super(PageIndicator.DIFF_UTIL_CALLBACK);
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.from(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), onClickListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ViewPageIndicatorBinding binding;

        public ViewHolder(@NonNull ViewPageIndicatorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(@NonNull PageIndicator pageIndicator, @NonNull PageIndicator.OnClickListener onClickListener) {
            binding.setPageIndicator(pageIndicator);
            binding.setOnClickListener(onClickListener);
            binding.executePendingBindings();
        }

        @NonNull
        public static ViewHolder from(@NonNull ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return new ViewHolder(ViewPageIndicatorBinding.inflate(inflater, parent, false));
        }
    }
}
