package de.pcps.jamtugether.model.soundtrack;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;
import timber.log.Timber;

public class CompositeSoundtrack extends Soundtrack {

    @NonNull
    private final List<SingleSoundtrack> soundtracks;

    public CompositeSoundtrack(@NonNull List<SingleSoundtrack> soundtracks) {
        super();
        this.soundtracks = soundtracks;
    }

    @NonNull
    public List<SingleSoundtrack> getSoundtracks() {
        return soundtracks;
    }

    @NonNull
    public static CompositeSoundtrack from(@NonNull List<SingleSoundtrack> soundtracks) {
        // the soundtracks are being cloned so the soundtracks in the
        // composite soundtrack list are not tied to the ones shown in the UI

        List<SingleSoundtrack> clonedList = new ArrayList<>();
        for(SingleSoundtrack singleSoundtrack : soundtracks) {
            try {
                clonedList.add(singleSoundtrack.clone());
            } catch (CloneNotSupportedException exception) {
                Timber.e(exception);
            }
        }
        return new CompositeSoundtrack(clonedList);
    }

    @Override
    public boolean isEmpty() {
        return soundtracks.isEmpty();
    }

    @Override
    public int getLength() {
        if(isEmpty()) {
            return 0;
        }
        int maxLength = 0;

        for(SingleSoundtrack singleSoundtrack : soundtracks) {
            int length = singleSoundtrack.getLength();
            if(length > maxLength) {
                maxLength = length;
            }
        }
        return maxLength;
    }

    @NonNull
    public List<Integer> getUserIDs() {
        List<Integer> userIDs = new ArrayList<>();
        for(SingleSoundtrack soundtrack : soundtracks) {
            userIDs.add(soundtrack.getUserID());
        }
        return userIDs;
    }
}
