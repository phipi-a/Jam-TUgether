package de.pcps.jamtugether.base.dagger;

import javax.inject.Singleton;

import dagger.Component;
import de.pcps.jamtugether.MainActivity;
import de.pcps.jamtugether.content.main_instrument.MainInstrumentFragment;
import de.pcps.jamtugether.content.main_instrument.MainInstrumentViewModel;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(MainActivity mainActivity);

    void inject(MainInstrumentFragment mainInstrumentFragment);

    void inject(MainInstrumentViewModel mainInstrumentViewModel);
}
