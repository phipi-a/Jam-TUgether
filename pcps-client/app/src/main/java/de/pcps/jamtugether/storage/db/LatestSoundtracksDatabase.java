package de.pcps.jamtugether.storage.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.drums.Drums;
import de.pcps.jamtugether.audio.instrument.flute.Flute;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;

@Singleton
public class LatestSoundtracksDatabase {

    @Nullable
    private SingleSoundtrack latestFluteSoundtrack;

    @Nullable
    private SingleSoundtrack latestDrumsSoundtrack;

    @Nullable
    private SingleSoundtrack latestShakerSoundtrack;

    @Inject
    public LatestSoundtracksDatabase(@NonNull RoomRepository roomRepository) {
        roomRepository.getUserInRoom().observeForever(userInRoom -> {
            if (!userInRoom) {
                onUserLeftRoom();
            }
        });
    }

    private void onUserLeftRoom() {
        latestFluteSoundtrack = null;
        latestDrumsSoundtrack = null;
        latestShakerSoundtrack = null;
    }

    public void onOwnSoundtrackUpdated(@NonNull SingleSoundtrack ownSoundtrack) {
        Instrument instrument = ownSoundtrack.getInstrument();
        if (instrument == Flute.getInstance()) {
            latestFluteSoundtrack = ownSoundtrack;
        } else if (instrument == Drums.getInstance()) {
            latestDrumsSoundtrack = ownSoundtrack;
        } else {
            latestShakerSoundtrack = ownSoundtrack;
        }
    }

    @Nullable
    public SingleSoundtrack getLatestSoundtrack(@NonNull Instrument instrument) {
        if (instrument == Flute.getInstance()) {
            return latestFluteSoundtrack;
        } else if (instrument == Drums.getInstance()) {
            return latestDrumsSoundtrack;
        } else {
            return latestShakerSoundtrack;
        }
    }
}
