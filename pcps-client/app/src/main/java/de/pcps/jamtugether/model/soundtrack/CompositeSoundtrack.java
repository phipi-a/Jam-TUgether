package de.pcps.jamtugether.model.soundtrack;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;

public class CompositeSoundtrack extends Soundtrack {

    @NonNull
    private final List<SingleSoundtrack> soundtracks;

    public CompositeSoundtrack(@NonNull List<SingleSoundtrack> soundtracks) {
        super();
        this.soundtracks = soundtracks;
    }

    @Override
    public int getLength() {
        if (isEmpty()) {
            return 0;
        }
        int maxLength = 0;

        for (SingleSoundtrack singleSoundtrack : soundtracks) {
            int length = singleSoundtrack.getLength();
            if (length > maxLength) {
                maxLength = length;
            }
        }
        return maxLength;
    }

    @Override
    public boolean isEmpty() {
        return soundtracks.isEmpty();
    }

    @NonNull
    public List<Integer> getUserIDs() {
        List<Integer> userIDs = new ArrayList<>();
        for (SingleSoundtrack soundtrack : soundtracks) {
            userIDs.add(soundtrack.getUserID());
        }
        return userIDs;
    }

    @NonNull
    public List<SingleSoundtrack> getSoundtracks() {
        return soundtracks;
    }

    @NonNull
    public static CompositeSoundtrack from(@NonNull List<SingleSoundtrack> soundtracks, @NonNull Context context) {
        // the soundtracks are being cloned so the soundtracks in the
        // composite soundtrack list are not tied to the ones shown in the UI

        List<SingleSoundtrack> clonedList = new ArrayList<>();
        for (SingleSoundtrack singleSoundtrack : soundtracks) {
            clonedList.add(singleSoundtrack.clone(context));
        }
        return new CompositeSoundtrack(clonedList);
    }
}
