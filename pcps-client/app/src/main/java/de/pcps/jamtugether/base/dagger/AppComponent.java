package de.pcps.jamtugether.base.dagger;

import javax.inject.Singleton;

import dagger.Component;
import de.pcps.jamtugether.MainActivity;
import de.pcps.jamtugether.content.welcome.WelcomeFragment;
import de.pcps.jamtugether.content.welcome.WelcomeViewModel;
import de.pcps.jamtugether.content.menu.MenuFragment;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(MainActivity mainActivity);

    void inject(WelcomeFragment welcomeFragment);

    void inject(WelcomeViewModel welcomeViewModel);

    void inject(MenuFragment menuFragment);
}
