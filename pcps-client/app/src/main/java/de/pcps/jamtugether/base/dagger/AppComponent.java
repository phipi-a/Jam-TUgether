package de.pcps.jamtugether.base.dagger;

import javax.inject.Singleton;

import dagger.Component;
import de.pcps.jamtugether.MainActivity;
import de.pcps.jamtugether.content.room.users.MusicianViewViewModel;
import de.pcps.jamtugether.content.settings.SettingsViewModel;
import de.pcps.jamtugether.content.welcome.WelcomeViewModel;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(MainActivity mainActivity);

    void inject(WelcomeViewModel welcomeViewModel);

    void inject(SettingsViewModel settingsViewModel);

    void inject(MusicianViewViewModel musicianViewViewModel);
}
