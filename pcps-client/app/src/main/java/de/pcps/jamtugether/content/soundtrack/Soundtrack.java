package de.pcps.jamtugether.content.soundtrack;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Soundtrack extends ArrayList<Sound> {

    public interface ClickListener {

        void onVolumeChanged(@NonNull Soundtrack soundtrack, int volumePercentage);

        void onPlayButtonClicked(@NonNull Soundtrack soundtrack);

        void onPauseButtonClicked(@NonNull Soundtrack soundtrack);

        void onStopButtonClicked(@NonNull Soundtrack soundtrack);

        void onFastForwardButtonClicked(@NonNull Soundtrack soundtrack);

        void onFastRewindButtonClicked(@NonNull Soundtrack soundtrack);

        void onClearButtonClicked(@NonNull Soundtrack soundtrack);
    }
}
