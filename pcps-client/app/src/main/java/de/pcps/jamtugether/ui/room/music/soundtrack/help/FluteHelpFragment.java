package de.pcps.jamtugether.ui.room.music.soundtrack.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.ui.base.BaseDialogFragment;

public class FluteHelpFragment extends BaseDialogFragment {

    @NonNull
    public static FluteHelpFragment newInstance() {
        return new FluteHelpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.on_boarding_flute_fragment, container, false);
    }
}
