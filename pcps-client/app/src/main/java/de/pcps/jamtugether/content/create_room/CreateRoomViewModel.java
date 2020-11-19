package de.pcps.jamtugether.content.create_room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import de.pcps.jamtugether.content.main_instrument.instrument.Instrument;





    public class CreateRoomViewModel extends AndroidViewModel {

        private final MutableLiveData<Boolean> navigateToJamRoom = new MutableLiveData<>(false);


        public LiveData<Boolean> getNavigateToJamRoom() {
            return navigateToJamRoom;
        }

        public CreateRoomViewModel(@NonNull Application application) {
            super(application);
        }

        public void onCreateRoomButtonClicked() {
            navigateToJamRoom.setValue(true);
        }

        public void onNavigatedToJamRoom() {
            navigateToJamRoom.setValue(false);
        }



    }


