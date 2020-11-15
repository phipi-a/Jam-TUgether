package de.pcps.jamtugether.navigation;

import androidx.navigation.NavController;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NavigationManager {

    @Inject
    public NavigationManager() { }

    public void navigateBack(NavController nc) {
        nc.popBackStack();
    }
}
