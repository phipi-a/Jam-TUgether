package de.pcps.jamtugether.model.soundtrack;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.pcps.jamtugether.R;
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
    @Override
    public String getLabel(@NonNull Context context) {
        return context.getString(R.string.composite);
    }

    @NonNull
    public List<String> getIDs() {
        List<String> compositeIDs = new ArrayList<>();
        for (SingleSoundtrack soundtrack : soundtracks) {
            compositeIDs.add(soundtrack.getID());
        }
        return compositeIDs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompositeSoundtrack that = (CompositeSoundtrack) o;
        return soundtracks.equals(that.soundtracks) && this.getProgress().getValue().equals(that.getProgress().getValue()) && this.getState().getValue() == that.getState().getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(soundtracks);
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
