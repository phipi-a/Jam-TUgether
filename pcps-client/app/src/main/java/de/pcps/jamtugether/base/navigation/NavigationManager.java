package de.pcps.jamtugether.base.navigation;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.pcps.jamtugether.content.menu.MenuFragmentDirections;
import de.pcps.jamtugether.content.welcome.WelcomeFragmentDirections;

@Singleton
public class NavigationManager {

    @Inject
    public NavigationManager() { }

    public void navigateBack(@NonNull NavController nc) {
        nc.popBackStack();
    }

    public void navigateToMenu(@NonNull NavController nc) {
        nc.navigate(WelcomeFragmentDirections.actionWelcomeFragmentToMenuFragment());
    }

    public void navigateToCreateRoom(@NonNull NavController nc) {
        nc.navigate(MenuFragmentDirections.actionMenuFragmentToCreateRoomFragment());
    }

    public void navigateToJoinRoom(@NonNull NavController nc) {
        nc.navigate(MenuFragmentDirections.actionMenuFragmentToJoinRoomFragment());
    }
}
