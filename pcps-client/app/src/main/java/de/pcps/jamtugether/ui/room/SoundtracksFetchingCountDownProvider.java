package de.pcps.jamtugether.ui.room;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

public interface SoundtracksFetchingCountDownProvider {

    @NonNull
    LiveData<Integer> getProgress();

    @NonNull
    LiveData<String> getCountDownText();
}
