package de.pcps.jamtugether.storage.db;

import androidx.annotation.NonNull;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.api.repositories.RoomRepository;
import de.pcps.jamtugether.model.soundtrack.SingleSoundtrack;

/**
 * keeps track of the volumes the user set for each soundtrack
 */
@Singleton
public class SoundtrackVolumesDatabase {

    private final float DEFAULT_VOLUME = 100;

    /**
     * maps id of soundtrack to its volume
     */
    @NonNull
    private final HashMap<String, Float> volumeMap = new HashMap<>();

    private float compositeSoundtrackVolume = DEFAULT_VOLUME;

    @Inject
    public SoundtrackVolumesDatabase(@NonNull RoomRepository roomRepository) {
        roomRepository.getUserInRoom().observeForever(userInRoom -> {
            if (!userInRoom) {
                onUserLeftRoom();
            }
        });
    }

    public void onUserLeftRoom() {
        compositeSoundtrackVolume = DEFAULT_VOLUME;
        volumeMap.clear();
    }

    public void onVolumeChanged(@NonNull SingleSoundtrack soundtrack, float volume) {
        volumeMap.put(soundtrack.getID(), volume);
    }

    public void onCompositeSoundtrackVolumeChanged(float volume) {
        this.compositeSoundtrackVolume = volume;
    }

    public float getVolumeOf(@NonNull SingleSoundtrack soundtrack) {
        Float volume = volumeMap.get(soundtrack.getID());
        return volume != null ? volume : DEFAULT_VOLUME;
    }

    public float getCompositeSoundtrackVolume() {
        return compositeSoundtrackVolume;
    }
}
