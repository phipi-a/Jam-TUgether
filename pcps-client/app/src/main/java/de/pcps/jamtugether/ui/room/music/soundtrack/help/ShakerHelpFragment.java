package de.pcps.jamtugether.ui.room.music.soundtrack.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.ui.base.BaseDialogFragment;

public class ShakerHelpFragment extends BaseDialogFragment {

    @NonNull
    public static ShakerHelpFragment newInstance() {
        return new ShakerHelpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.on_boarding_shaker_fragment, container, false);
    }
}
