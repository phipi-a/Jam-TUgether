package de.pcps.jamtugether.ui.room.music.instrument.piano.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.ArrayList;
import java.util.List;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.audio.instrument.piano.Piano;
import de.pcps.jamtugether.audio.instrument.piano.PianoSound;
import de.pcps.jamtugether.utils.UiUtils;

public class PianoView extends ConstraintLayout {

    @Nullable
    private Piano.OnKeyListener onKeyListener;

    @NonNull
    private final int[] generatedWhiteTileIDs = new int[PianoSound.WHITE_TILE_SOUNDS.length];

    @NonNull
    private final int[] generatedBlackTileIDs = new int[PianoSound.BLACK_TILE_SOUNDS.length];

    @NonNull
    private final ConstraintSet constraintSet;

    @NonNull
    private final ViewGroup.LayoutParams blackTilesLayoutParams;

    @Nullable
    private List<View> tiles;

    private boolean drawn;

    public PianoView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setWillNotDraw(false);
        this.constraintSet = new ConstraintSet();
        this.blackTilesLayoutParams = new ViewGroup.LayoutParams(0, 0);
    }

    public void setOnKeyListener(@NonNull Piano.OnKeyListener onKeyListener) {
        this.onKeyListener = onKeyListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int[] ids = new int[PianoSound.WHITE_TILE_SOUNDS.length];
        float[] weights = new float[PianoSound.WHITE_TILE_SOUNDS.length];

        int whiteTileHeight = UiUtils.getPixels(getContext(), R.dimen.piano_white_tile_height);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(0, whiteTileHeight);
        int index = 0;
        for (PianoSound pianoSound : PianoSound.WHITE_TILE_SOUNDS) {
            View whiteTile = getWhiteTile(pianoSound);
            int id = View.generateViewId();
            whiteTile.setId(id);
            generatedWhiteTileIDs[index] = id;
            this.addView(whiteTile, params);
            ids[index] = whiteTile.getId();
            weights[index++] = 1f;
        }

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        constraintSet.createHorizontalChain(R.id.piano_view, ConstraintSet.LEFT, R.id.piano_view, ConstraintSet.RIGHT, ids, weights, ConstraintSet.CHAIN_SPREAD);
        constraintSet.applyTo(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (drawn) {
            return;
        }

        int whiteTileWidth = (int) (this.getWidth() / 8.0);
        int blackTileWidth = (int) (whiteTileWidth / 1.5);
        int blackTileHeight = UiUtils.getPixels(getContext(), R.dimen.piano_black_tile_height);
        int pianoViewPaddingBottom = UiUtils.getPixels(getContext(), R.dimen.piano_view_padding_bottom);

        blackTilesLayoutParams.width = blackTileWidth;
        blackTilesLayoutParams.height = blackTileHeight;
        int index = 0;
        for (PianoSound pianoSound : PianoSound.BLACK_TILE_SOUNDS) {
            View blackTile = getBlackTile(pianoSound);

            int id = View.generateViewId();
            blackTile.setId(id);
            generatedBlackTileIDs[index++] = id;
            this.addView(blackTile, blackTilesLayoutParams);
        }

        View cTile = findViewById(generatedWhiteTileIDs[0]);
        View cSharpTile = findViewById(generatedBlackTileIDs[0]);
        View dTile = findViewById(generatedWhiteTileIDs[1]);
        View dSharpTile = findViewById(generatedBlackTileIDs[1]);
        View eTile = findViewById(generatedWhiteTileIDs[2]);
        View fTile = findViewById(generatedWhiteTileIDs[3]);
        View fSharpTile = findViewById(generatedBlackTileIDs[2]);
        View gTile = findViewById(generatedWhiteTileIDs[4]);
        View gSharpTile = findViewById(generatedBlackTileIDs[3]);
        View aTile = findViewById(generatedWhiteTileIDs[5]);
        View aSharpTile = findViewById(generatedBlackTileIDs[4]);
        View bTile = findViewById(generatedWhiteTileIDs[6]);
        View cHighTile = findViewById(generatedWhiteTileIDs[7]);

        if (tiles == null) {
            tiles = new ArrayList<>();
            tiles.add(cTile);
            tiles.add(cSharpTile);
            tiles.add(dTile);
            tiles.add(dSharpTile);
            tiles.add(eTile);
            tiles.add(fTile);
            tiles.add(fSharpTile);
            tiles.add(gTile);
            tiles.add(gSharpTile);
            tiles.add(aTile);
            tiles.add(aSharpTile);
            tiles.add(bTile);
            tiles.add(cHighTile);

            for (PianoSound pianoSound : PianoSound.values()) {
                View tile = tiles.get(pianoSound.ordinal());
                tile.setOnTouchListener((v, event) -> {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            onKeyListener.onKeyPressed(pianoSound.getPitch());
                            return true;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            onKeyListener.onKeyReleased(pianoSound.getPitch());
                            return true;
                    }
                    return false;
                });
            }
        }

        int blackTileMarginStart = whiteTileWidth - blackTileWidth / 2;

        constraintSet.clone(this);
        constraintSet.connect(cSharpTile.getId(), ConstraintSet.START, cTile.getId(), ConstraintSet.START, blackTileMarginStart);
        constraintSet.connect(dSharpTile.getId(), ConstraintSet.START, dTile.getId(), ConstraintSet.START, blackTileMarginStart);
        constraintSet.connect(fSharpTile.getId(), ConstraintSet.START, fTile.getId(), ConstraintSet.START, blackTileMarginStart);
        constraintSet.connect(gSharpTile.getId(), ConstraintSet.START, gTile.getId(), ConstraintSet.START, blackTileMarginStart);
        constraintSet.connect(aSharpTile.getId(), ConstraintSet.START, aTile.getId(), ConstraintSet.START, blackTileMarginStart);
        constraintSet.applyTo(this);

        this.setPadding(0, 0, 0, pianoViewPaddingBottom);

        drawn = true;
    }

    @NonNull
    private TextView getWhiteTile(@NonNull PianoSound pianoSound) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        TextView textView = (TextView) inflater.inflate(R.layout.view_piano_white_tile, this, false);
        textView.setText(pianoSound.getLabel(getContext()));
        return textView;
    }

    @NonNull
    private TextView getBlackTile(@NonNull PianoSound pianoSound) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        TextView textView = (TextView) inflater.inflate(R.layout.view_piano_black_tile, this, false);
        textView.setText(pianoSound.getLabel(getContext()));
        return textView;
    }
}
