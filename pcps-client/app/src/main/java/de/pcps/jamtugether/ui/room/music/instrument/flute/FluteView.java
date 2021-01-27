package de.pcps.jamtugether.ui.room.music.instrument.flute;

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

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.sound.flute.FluteSound;

public class FluteView extends ConstraintLayout {

    private LinearLayout noteLabelsLayout;

    @NonNull
    private final ConstraintSet constraintSet;

    public FluteView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.constraintSet = new ConstraintSet();
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
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        int noteLabelHeight = noteLabelsLayout.getChildAt(0).getHeight();

        double labelDistance = (this.getHeight() - noteLabelHeight * FluteSound.values().length) / (double) (FluteSound.values().length - 1);

        float y = noteLabelsLayout.getY();
        for (int index = 0; index < FluteSound.values().length; index++) {
            View view = noteLabelsLayout.getChildAt(index);
            view.setY(y);
            y += noteLabelHeight + labelDistance;
        }

        // update constraints
        constraintSet.clone(this);
        constraintSet.connect(R.id.iv_flute, ConstraintSet.TOP, R.id.note_labels_layout, ConstraintSet.TOP, noteLabelHeight / 2);
        constraintSet.connect(R.id.iv_flute, ConstraintSet.BOTTOM, R.id.note_labels_layout, ConstraintSet.BOTTOM, noteLabelHeight / 2);
        constraintSet.connect(R.id.iv_flute, ConstraintSet.START, R.id.note_labels_layout, ConstraintSet.START);
        constraintSet.connect(R.id.iv_flute, ConstraintSet.END, R.id.note_labels_layout, ConstraintSet.END);
        constraintSet.applyTo(this);
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
