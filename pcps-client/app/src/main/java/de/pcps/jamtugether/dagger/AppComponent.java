package de.pcps.jamtugether.dagger;

import javax.inject.Singleton;

import dagger.Component;
import de.pcps.jamtugether.MainActivity;
import de.pcps.jamtugether.content.room.create.CreateRoomViewModel;
import de.pcps.jamtugether.content.room.join.JoinRoomViewModel;
import de.pcps.jamtugether.content.room.users.music.MusicianViewViewModel;
import de.pcps.jamtugether.content.room.users.admin.AdminRoomOverviewViewModel;
import de.pcps.jamtugether.content.room.users.music.soundtrack.SoundtrackViewModel;
import de.pcps.jamtugether.content.room.users.regular.RegularRoomOverviewViewModel;
import de.pcps.jamtugether.content.settings.SettingsViewModel;
import de.pcps.jamtugether.content.welcome.WelcomeViewModel;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(MainActivity mainActivity);

    void inject(WelcomeViewModel welcomeViewModel);

    void inject(SettingsViewModel settingsViewModel);

    void inject(CreateRoomViewModel createRoomViewModel);

    void inject(JoinRoomViewModel joinRoomViewModel);

    void inject(AdminRoomOverviewViewModel adminRoomOverviewViewModel);

    void inject(RegularRoomOverviewViewModel regularRoomOverviewViewModel);

    void inject(SoundtrackViewModel soundtrackViewModel);
}
