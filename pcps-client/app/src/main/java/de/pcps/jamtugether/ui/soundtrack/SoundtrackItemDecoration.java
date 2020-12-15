package de.pcps.jamtugether.ui.soundtrack;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.utils.UiUtils;

public class SoundtrackItemDecoration extends RecyclerView.ItemDecoration {

    @NonNull
    private final Context context;

    public SoundtrackItemDecoration(@NonNull Context context) {
        super();
        this.context = context;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top = UiUtils.getPixels(context, R.dimen.soundtrack_item_margin_top);

        if(parent.getChildAdapterPosition(view) == state.getItemCount() - 1) { // last item
            outRect.bottom = UiUtils.getPixels(context, R.dimen.soundtrack_last_item_bottom);
        }
    }
}
