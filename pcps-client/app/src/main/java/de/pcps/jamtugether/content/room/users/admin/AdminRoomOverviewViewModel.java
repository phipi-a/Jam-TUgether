package de.pcps.jamtugether.content.room.users.admin;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.pcps.jamtugether.R;
import de.pcps.jamtugether.content.soundtrack.Soundtrack;
import de.pcps.jamtugether.dagger.AppInjector;

public class AdminRoomOverviewViewModel extends ViewModel implements Soundtrack.OnChangeListener {

    @Inject
    Application application;

    private final int roomID;

    @NonNull
    private final MutableLiveData<Drawable> playPauseButtonDrawable;

    @NonNull
    private final MutableLiveData<Integer> stopButtonVisibility = new MutableLiveData<>(View.INVISIBLE);

    @NonNull
    private final MutableLiveData<List<Soundtrack>> allSoundtracks = new MutableLiveData<>(generateTestSoundtracks());

    public AdminRoomOverviewViewModel(int roomID) {
        AppInjector.inject(this);
        this.roomID = roomID;
        playPauseButtonDrawable = new MutableLiveData<>(ContextCompat.getDrawable(application.getApplicationContext(), R.drawable.ic_play));
    }

    @NonNull
    private List<Soundtrack> generateTestSoundtracks() {
        List<Soundtrack> list = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            list.add(new Soundtrack(i));
        }
        return list;
    }

    @Override
    public void onVolumeChanged(@NonNull Soundtrack soundtrack, float volume) {
        soundtrack.setVolume(volume);
    }

    @Override
    public void onPlayPauseButtonClicked(@NonNull Soundtrack soundtrack) {
        // todo update stop button visibility
        // todo update play button drawable
    }

    @Override
    public void onStopButtonClicked(@NonNull Soundtrack soundtrack) { }

    @Override
    public void onFastForwardButtonClicked(@NonNull Soundtrack soundtrack) { }

    @Override
    public void onFastRewindButtonClicked(@NonNull Soundtrack soundtrack) { }

    @Override
    public void onDeleteButtonClicked(@NonNull Soundtrack soundtrack) {
        // todo update sound track list if sound track is in the list
        deleteSoundtrack();
    }

    private void deleteSoundtrack() {
        // todo delete soundtrack on server
    }

    private void fetchAllSoundtracks() {
        // todo get all soundtracks from server and update current list after
    }

    public int getRoomID() {
        return roomID;
    }

    @NonNull
    public LiveData<Drawable> getPlayPauseButtonDrawable() {
        return playPauseButtonDrawable;
    }

    @NonNull
    public LiveData<Integer> getStopButtonVisibility() {
        return stopButtonVisibility;
    }

    @NonNull
    public LiveData<Soundtrack> getCompositeSoundtrack() {
        return Transformations.map(getAllSoundtracks(), Soundtrack::compositeFrom);
    }

    @NonNull
    public LiveData<List<Soundtrack>> getAllSoundtracks() {
        return allSoundtracks;
    }

    static class Factory implements ViewModelProvider.Factory {

        private final int roomID;

        public Factory(int roomID) {
            this.roomID = roomID;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(AdminRoomOverviewViewModel.class)) {
                return (T) new AdminRoomOverviewViewModel(roomID);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
