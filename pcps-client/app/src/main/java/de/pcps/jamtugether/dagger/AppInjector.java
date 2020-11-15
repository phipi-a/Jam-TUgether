package de.pcps.jamtugether.dagger;

import de.pcps.jamtugether.MyApplication;

public class AppInjector {

    private static AppComponent appComponent;

    public static void buildAppComponent(MyApplication application) {
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(application)).build();
    }
}
