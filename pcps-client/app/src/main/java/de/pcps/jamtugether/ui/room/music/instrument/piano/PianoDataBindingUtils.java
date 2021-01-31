package de.pcps.jamtugether.ui.room.music.instrument.piano;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import de.pcps.jamtugether.audio.instrument.piano.Piano;
import de.pcps.jamtugether.ui.room.music.instrument.piano.view.PianoView;

public class PianoDataBindingUtils {

    @BindingAdapter("onKeyListener")
    public static void setClickListener(@NonNull PianoView pianoView, @NonNull Piano.OnKeyListener onKeyListener) {
        pianoView.setOnKeyListener(onKeyListener);
    }
}
