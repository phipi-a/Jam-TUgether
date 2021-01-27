package de.pcps.jamtugether.di;

import javax.inject.Singleton;

import dagger.Component;
import de.pcps.jamtugether.MainActivity;
import de.pcps.jamtugether.ui.menu.create.CreateRoomViewModel;
import de.pcps.jamtugether.ui.menu.join.JoinRoomViewModel;
import de.pcps.jamtugether.ui.onboarding.OnBoardingViewModel;
import de.pcps.jamtugether.ui.onboarding.screens.instrument.ChooseMainInstrumentViewModel;
import de.pcps.jamtugether.ui.room.RoomViewModel;
import de.pcps.jamtugether.ui.room.music.instrument.InstrumentViewModel;
import de.pcps.jamtugether.ui.room.music.soundtrack.OwnSoundtrackFragment;
import de.pcps.jamtugether.ui.room.music.soundtrack.OwnSoundtrackViewModel;
import de.pcps.jamtugether.ui.room.overview.SoundtrackOverviewFragment;
import de.pcps.jamtugether.ui.room.overview.SoundtrackOverviewViewModel;
import de.pcps.jamtugether.ui.settings.SettingsViewModel;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(MainActivity mainActivity);

    void inject(OnBoardingViewModel onBoardingViewModel);

    void inject(ChooseMainInstrumentViewModel chooseMainInstrumentViewModel);

    void inject(SettingsViewModel settingsViewModel);

    void inject(CreateRoomViewModel createRoomViewModel);

    void inject(JoinRoomViewModel joinRoomViewModel);

    void inject(RoomViewModel roomViewModel);

    void inject(SoundtrackOverviewFragment soundtrackOverviewFragment);

    void inject(SoundtrackOverviewViewModel soundtrackOverviewViewModel);

    void inject(OwnSoundtrackFragment ownSoundtrackFragment);

    void inject(OwnSoundtrackViewModel ownSoundtrackViewModel);

    void inject(InstrumentViewModel instrumentViewModel);
}
