package de.pcps.jamtugether.storage.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.audio.instrument.base.Instrument;
import de.pcps.jamtugether.audio.instrument.drums.Drums;
import de.pcps.jamtugether.audio.instrument.flute.Flute;
import de.pcps.jamtugether.audio.instrument.shaker.Shaker;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;

@Singleton
public class LatestSoundtracksDatabase {

    @Nullable
    private SingleSoundtrack latestFluteSoundtrack;

    @Nullable
    private SingleSoundtrack latestDrumsSoundtrack;

    @Nullable
    private SingleSoundtrack latestShakerSoundtrack;

    @Nullable
    private SingleSoundtrack latestPianoSoundtrack;

    private boolean fluteSoundtrackUploaded;
    private boolean drumsSoundtrackUploaded;
    private boolean shakerSoundtrackUploaded;
    private boolean pianoSoundtrackUploaded;

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
        latestPianoSoundtrack = null;

        fluteSoundtrackUploaded = false;
        drumsSoundtrackUploaded = false;
        shakerSoundtrackUploaded = false;
        pianoSoundtrackUploaded = false;
    }

    public void onOwnSoundtrackUpdated(@NonNull SingleSoundtrack ownSoundtrack) {
        Instrument instrument = ownSoundtrack.getInstrument();
        if (instrument == Flute.getInstance()) {
            latestFluteSoundtrack = ownSoundtrack;
        } else if (instrument == Drums.getInstance()) {
            latestDrumsSoundtrack = ownSoundtrack;
        } else if (instrument == Shaker.getInstance()) {
            latestShakerSoundtrack = ownSoundtrack;
        } else {
            latestPianoSoundtrack = ownSoundtrack;
        }
    }

    public void onOwnSoundtrackUploaded(@NonNull SingleSoundtrack ownSoundtrack) {
        Instrument instrument = ownSoundtrack.getInstrument();
        if (instrument == Flute.getInstance()) {
            fluteSoundtrackUploaded = true;
        } else if (instrument == Drums.getInstance()) {
            drumsSoundtrackUploaded = true;
        } else if (instrument == Shaker.getInstance()) {
            shakerSoundtrackUploaded = true;
        } else {
            pianoSoundtrackUploaded = true;
        }
    }

    @Nullable
    public SingleSoundtrack getLatestSoundtrack(@NonNull Instrument instrument) {
        if (instrument == Flute.getInstance()) {
            return latestFluteSoundtrack;
        } else if (instrument == Drums.getInstance()) {
            return latestDrumsSoundtrack;
        } else if (instrument == Shaker.getInstance()) {
            return latestShakerSoundtrack;
        } else {
            return latestPianoSoundtrack;
        }
    }

    public boolean getLatestSoundtrackUploaded(@NonNull Instrument instrument) {
        if (instrument == Flute.getInstance()) {
            return fluteSoundtrackUploaded;
        } else if (instrument == Drums.getInstance()) {
            return drumsSoundtrackUploaded;
        } else if (instrument == Shaker.getInstance()) {
            return shakerSoundtrackUploaded;
        } else {
            return pianoSoundtrackUploaded;
        }
    }
}
