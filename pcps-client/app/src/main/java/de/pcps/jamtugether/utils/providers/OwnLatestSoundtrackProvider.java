package de.pcps.jamtugether.utils.providers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.drums.Drums;
import de.pcps.jamtugether.audio.instrument.flute.Flute;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;

@Singleton
public class OwnLatestSoundtrackProvider {

    @Nullable
    private SingleSoundtrack ownLatestFluteSoundtrack;

    @Nullable
    private SingleSoundtrack ownLatestDrumsSoundtrack;

    @Nullable
    private SingleSoundtrack ownLatestShakerSoundtrack;

    @Inject
    public OwnLatestSoundtrackProvider() { }

    public void onUserLeftRoom() {
        ownLatestShakerSoundtrack = null;
        ownLatestDrumsSoundtrack = null;
        ownLatestShakerSoundtrack = null;
    }

    public void onOwnSoundtrackUpdated(@NonNull SingleSoundtrack ownSoundtrack) {
        Instrument instrument = ownSoundtrack.getInstrument();
        if(instrument == Flute.getInstance()) {
            ownLatestFluteSoundtrack = ownSoundtrack;
        } else if(instrument == Drums.getInstance()) {
            ownLatestDrumsSoundtrack = ownSoundtrack;
        } else {
            ownLatestShakerSoundtrack = ownSoundtrack;
        }
    }

    @Nullable
    public SingleSoundtrack getOwnLatestSoundtrack(@NonNull Instrument instrument) {
        if(instrument == Flute.getInstance()) {
            return ownLatestFluteSoundtrack;
        } else if(instrument == Drums.getInstance()) {
            return ownLatestDrumsSoundtrack;
        } else {
            return ownLatestShakerSoundtrack;
        }
    }
}
