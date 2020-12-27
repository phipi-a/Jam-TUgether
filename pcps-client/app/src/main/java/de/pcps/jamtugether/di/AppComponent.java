package de.pcps.jamtugether.di;

import javax.inject.Singleton;

import dagger.Component;
import de.pcps.jamtugether.MainActivity;
import de.pcps.jamtugether.ui.menu.create.CreateRoomViewModel;
import de.pcps.jamtugether.ui.menu.join.JoinRoomViewModel;
import de.pcps.jamtugether.ui.room.music.MusicianViewViewModel;
import de.pcps.jamtugether.ui.room.music.instrument.drums.DrumsViewModel;
import de.pcps.jamtugether.ui.room.music.instrument.flute.FluteViewModel;
import de.pcps.jamtugether.ui.room.music.soundtrack.SoundtrackViewModel;
import de.pcps.jamtugether.ui.room.overview.RoomOverviewViewModel;
import de.pcps.jamtugether.ui.settings.SettingsViewModel;
import de.pcps.jamtugether.ui.welcome.WelcomeViewModel;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(MainActivity mainActivity);

    void inject(WelcomeViewModel welcomeViewModel);

    void inject(SettingsViewModel settingsViewModel);

    void inject(CreateRoomViewModel createRoomViewModel);

    void inject(JoinRoomViewModel joinRoomViewModel);

    void inject(RoomOverviewViewModel roomOverviewViewModel);

    void inject(SoundtrackViewModel soundtrackViewModel);
}
