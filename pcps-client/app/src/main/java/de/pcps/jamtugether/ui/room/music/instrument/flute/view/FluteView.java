package de.pcps.jamtugether.ui.room.music.instrument.flute.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.flute.FluteSound;
import de.pcps.jamtugether.utils.UiUtils;

public class FluteView extends ConstraintLayout {

    @NonNull
    private final ConstraintSet constraintSet;

    @NonNull
    private final ConstraintLayout.LayoutParams noteLabelsLayoutParams;

    private LinearLayout noteLabelsLayout;

    private boolean soundtracksExpanded;

    public FluteView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setWillNotDraw(false);
        this.constraintSet = new ConstraintSet();
        this.noteLabelsLayoutParams = new Constraints.LayoutParams(0, 0);
    }

    public void setSoundtracksExpanded(boolean soundtracksExpanded) {
        this.soundtracksExpanded = soundtracksExpanded;
        this.invalidate();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        noteLabelsLayout = this.findViewById(R.id.note_labels_layout);
        for (int i = FluteSound.values().length - 1; i >= 0; i--) {
            FluteSound fluteSound = FluteSound.values()[i];
            int index = FluteSound.values().length - 1 - i;
            noteLabelsLayout.addView(createNoteLabel(fluteSound), index);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        View fluteFragmentView = (View) this.getParent();
        View linearLayout = (View) fluteFragmentView.getParent().getParent();
        View parent = (View) linearLayout.getParent();

        int ownSoundtrackViewHeight = linearLayout.findViewById(R.id.own_soundtrack_fragment_container).getHeight();
        int ownSoundtrackControlsHeight = fluteFragmentView.findViewById(R.id.own_soundtrack_controls_layout).getHeight();
        int ownSoundtrackControlsMarginTop = UiUtils.getPixels(getContext(), R.dimen.own_soundtrack_controls_margin_top);
        int fluteViewMarginTop = UiUtils.getPixels(getContext(), R.dimen.flute_view_margin_top);
        int fluteViewPaddingBottom = UiUtils.getPixels(getContext(), R.dimen.flute_view_padding_bottom);

        int fluteViewDefaultHeight = UiUtils.getPixels(getContext(), R.dimen.flute_view_default_height);
        int fluteViewHeight;
        if (soundtracksExpanded) {
            fluteViewHeight = fluteViewDefaultHeight;
        } else {
            int availableHeight = parent.getHeight() -
                    (ownSoundtrackViewHeight + ownSoundtrackControlsHeight + ownSoundtrackControlsMarginTop + fluteViewMarginTop + fluteViewPaddingBottom);

            fluteViewHeight = Math.max(availableHeight, fluteViewDefaultHeight);
        }

        int noteLabelHeight = noteLabelsLayout.getChildAt(0).getHeight();

        double labelDistance = (fluteViewHeight - noteLabelHeight * FluteSound.values().length) / (double) (FluteSound.values().length - 1);

        float y = noteLabelsLayout.getY();
        for (int index = 0; index < FluteSound.values().length; index++) {
            View view = noteLabelsLayout.getChildAt(index);
            view.setY(y);
            y += noteLabelHeight + labelDistance;
        }

        noteLabelsLayoutParams.width = LayoutParams.WRAP_CONTENT;
        noteLabelsLayoutParams.height = fluteViewHeight;
        noteLabelsLayout.setLayoutParams(noteLabelsLayoutParams);

        constraintSet.clone(this);
        constraintSet.connect(R.id.note_labels_layout, ConstraintSet.TOP, R.id.flute_view, ConstraintSet.TOP);
        constraintSet.connect(R.id.note_labels_layout, ConstraintSet.START, R.id.flute_image_view, ConstraintSet.END);
        constraintSet.connect(R.id.flute_image_view, ConstraintSet.TOP, R.id.note_labels_layout, ConstraintSet.TOP, noteLabelHeight / 2);
        constraintSet.connect(R.id.flute_image_view, ConstraintSet.BOTTOM, R.id.note_labels_layout, ConstraintSet.BOTTOM, noteLabelHeight / 2);
        constraintSet.applyTo(this);

        this.setPadding(0, 0, 0, fluteViewPaddingBottom);
    }

    @NonNull
    private View createNoteLabel(@NonNull FluteSound sound) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ConstraintLayout noteLabel = (ConstraintLayout) inflater.inflate(R.layout.view_note_label, noteLabelsLayout, false);
        TextView noteLabelTextView = noteLabel.findViewById(R.id.note_label_text_view);
        noteLabelTextView.setText(sound.getLabel(getContext()));
        return noteLabel;
    }
}
