package de.pcps.jamtugether.ui.room.music.instrument.piano;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.HashMap;

import de.pcps.jamtugether.audio.instrument.piano.Piano;
import de.pcps.jamtugether.model.Sound;
import de.pcps.jamtugether.audio.instrument.piano.PianoSound;
import de.pcps.jamtugether.audio.sound.model.StreamID;
import de.pcps.jamtugether.ui.room.music.OnOwnSoundtrackChangedCallback;
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentViewModel;

public class PianoViewModel extends InstrumentViewModel implements Piano.OnKeyListener {

    @NonNull
    private static final Piano piano = Piano.getInstance();

    @NonNull
    private final HashMap<Integer, StreamID> pitchStreamIDsMap = new HashMap<>();

    public PianoViewModel(@NonNull OnOwnSoundtrackChangedCallback callback) {
        super(piano, callback);
    }

    @Override
    public void onKeyPressed(int pitch) {
        piano.play(pitch, streamID -> {
            if (streamID != 0) {
                int startTimeMillis = (int) (System.currentTimeMillis() - startedMillis);
                pitchStreamIDsMap.put(pitch, new StreamID(streamID, startTimeMillis));
            }
        });
    }

    @Override
    public void onKeyReleased(int pitch) {
        stopSound(pitch);
    }

    private void stopSound(int pitch) {
        StreamID streamID = pitchStreamIDsMap.get(pitch);
        if (streamID != null) {
            piano.stopSound(streamID.getStreamID());
            pitchStreamIDsMap.remove(pitch);
            if (ownSoundtrack != null) {
                int stoppedEndTimeMillis = (int) (System.currentTimeMillis() - startedMillis);
                int completeSoundEndTimeMillis = streamID.getStartTime() + PianoSound.from(pitch).getDuration();
                int endTimeMillis = Math.min(stoppedEndTimeMillis, completeSoundEndTimeMillis);
                Sound sound = new Sound(streamID.getStartTime(), endTimeMillis, pitch);
                ownSoundtrack.addSound(sound);
            }
        }
    }

    @Override
    protected void finishRecordingSoundtrack() {
        finishSound();
        super.finishRecordingSoundtrack();
    }

    private void finishSound() {
        piano.stop();
        for (Integer pitch : pitchStreamIDsMap.keySet()) {
            stopSound(pitch);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        finishSound();
    }

    static class Factory implements ViewModelProvider.Factory {

        @NonNull
        private final OnOwnSoundtrackChangedCallback callback;

        public Factory(@NonNull OnOwnSoundtrackChangedCallback callback) {
            this.callback = callback;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(PianoViewModel.class)) {
                return (T) new PianoViewModel(callback);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
