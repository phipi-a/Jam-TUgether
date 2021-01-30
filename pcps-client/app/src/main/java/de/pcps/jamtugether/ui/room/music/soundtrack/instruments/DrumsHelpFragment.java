package de.pcps.jamtugether.ui.room.music.soundtrack.instruments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import de.pcps.jamtugether.R;

public class DrumsHelpFragment extends DialogFragment {

    @NonNull
    public static DrumsHelpFragment newInstance() {
        return new DrumsHelpFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.on_boarding_drums_fragment, container, false);
    }
}
