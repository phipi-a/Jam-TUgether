package de.pcps.jamtugether.ui.room.music.instrument.flute;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.Constraints;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.model.sound.flute.FluteSound;
import de.pcps.jamtugether.utils.UiUtils;
import timber.log.Timber;

public class FluteView extends ConstraintLayout {

    private ImageView fluteImageView;

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
        //fluteImageView = this.findViewById(R.id.iv_flute);
        noteLabelsLayout = this.findViewById(R.id.note_labels_layout);
        for (int i = FluteSound.values().length - 1; i >= 0; i--) {
            FluteSound fluteSound = FluteSound.values()[i];
            int index = FluteSound.values().length - 1 - i;
            noteLabelsLayout.addView(createNoteLabelTextView(fluteSound), index);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        int fluteHeight = UiUtils.getPixels(getContext(), R.dimen.flute_height);

        double sectionHeight = fluteHeight / 12.0;
        float y = noteLabelsLayout.getY();
        for (int index = 0; index < FluteSound.values().length; index++) {
            View view = noteLabelsLayout.getChildAt(index);
            view.setY(y);
            y += sectionHeight;
        }

        int noteLabelHeight = noteLabelsLayout.getChildAt(0).getHeight();

        // update constraints
        this.setLayoutParams(new Constraints.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        constraintSet.clone(this);

        constraintSet.connect(R.id.iv_flute, ConstraintSet.TOP, R.id.note_labels_layout, ConstraintSet.TOP, noteLabelHeight / 2);
        constraintSet.connect(R.id.iv_flute, ConstraintSet.BOTTOM, R.id.note_labels_layout, ConstraintSet.BOTTOM, noteLabelHeight / 2);
        constraintSet.connect(R.id.iv_flute, ConstraintSet.START, R.id.note_labels_layout, ConstraintSet.START);
        constraintSet.connect(R.id.iv_flute, ConstraintSet.END, R.id.note_labels_layout, ConstraintSet.END);
        constraintSet.connect(R.id.note_labels_layout, ConstraintSet.TOP, this.getId(), ConstraintSet.TOP);
        constraintSet.connect(R.id.note_labels_layout, ConstraintSet.BOTTOM, this.getId(), ConstraintSet.BOTTOM);
        constraintSet.connect(R.id.note_labels_layout, ConstraintSet.START, R.id.note_labels_layout, ConstraintSet.START);
        constraintSet.applyTo(this);

    }

    @NonNull
    private TextView createNoteLabelTextView(@NonNull FluteSound sound) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        TextView textView = (TextView) inflater.inflate(R.layout.view_note_label, this, false);
        textView.setText(sound.getLabel(getContext()));
        return textView;
    }
}
