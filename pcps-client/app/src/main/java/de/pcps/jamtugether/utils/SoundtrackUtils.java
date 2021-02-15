package de.pcps.jamtugether.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import de.pcps.jamtugether.model.User;
import de.pcps.jamtugether.model.soundtrack.CompositeSoundtrack;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;
import de.pcps.jamtugether.model.soundtrack.base.Soundtrack;

public class SoundtrackUtils {

    @NonNull
    public static CompositeSoundtrack createCompositeSoundtrack(@Nullable CompositeSoundtrack previousCompositeSoundtrack, @NonNull List<SingleSoundtrack> soundtracks, @NonNull Context context) {
        CompositeSoundtrack newCompositeSoundtrack = CompositeSoundtrack.from(soundtracks, context);
        if (previousCompositeSoundtrack != null) {
            Soundtrack.State previousState = previousCompositeSoundtrack.getState().getValue();
            int previousProgressInMillis = previousCompositeSoundtrack.getProgressInMillis();
            if (previousState != null) {
                newCompositeSoundtrack.setState(previousState);
            }
            newCompositeSoundtrack.setProgressInMillis(previousProgressInMillis);
        }
        return newCompositeSoundtrack;
    }

    @NonNull
    public static List<SingleSoundtrack> getOwnDeletedSoundtracks(@NonNull User user, @NonNull List<SingleSoundtrack> previousSoundtracks, @NonNull List<SingleSoundtrack> currentSoundtracks) {
        List<SingleSoundtrack> ownDeletedSoundtracks = new ArrayList<>();
        for (SingleSoundtrack soundtrack : previousSoundtracks) {
            if (!isInList(soundtrack, currentSoundtracks) && soundtrack.getUserID() == user.getID()) {
                ownDeletedSoundtracks.add(soundtrack);
            }
        }
        return ownDeletedSoundtracks;
    }

    private static boolean isInList(@NonNull SingleSoundtrack soundtrack, @NonNull List<SingleSoundtrack> list) {
        for (SingleSoundtrack element : list) {
            if (element.getID().equals(soundtrack.getID())) {
                return true;
            }
        }
        return false;
    }
}
