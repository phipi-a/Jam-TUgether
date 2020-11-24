package de.pcps.jamtugether.content.room.users.regular;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class MusicianViewModel extends AndroidViewModel {

    public MusicianViewModel(@NonNull Application application) {
        super(application);
    }

    private final MutableLiveData<Boolean> showHelpDialog = new MutableLiveData<>(false);

    public void onHelpButtonClicked() {
        showHelpDialog.setValue(true);
    }


    public void onHelpDialogShown() {
        showHelpDialog.setValue(false);
    }

    @NonNull
    public LiveData<Boolean> getShowHelpDialog() {
        return showHelpDialog;
    }
}
