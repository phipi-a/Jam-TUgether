package de.pcps.jamtugether.utils;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;

import de.pcps.jamtugether.content.menu.MenuFragmentDirections;
import de.pcps.jamtugether.content.menu.create.CreateRoomFragmentDirections;
import de.pcps.jamtugether.content.menu.join.JoinRoomFragmentDirections;
import de.pcps.jamtugether.content.welcome.WelcomeFragmentDirections;

public class NavigationUtils {

    public static void navigateToMenu(@NonNull NavController nc) {
        nc.navigate(WelcomeFragmentDirections.actionWelcomeFragmentToMenuFragment());
    }

    public static void navigateToSettings(@NonNull NavController nc) {
        nc.navigate(MenuFragmentDirections.actionMenuFragmentToSettingsFragment());
    }

    public static void navigateToCreateRoom(@NonNull NavController nc) {
        nc.navigate(MenuFragmentDirections.actionMenuFragmentToCreateRoomFragment());
    }

    public static void navigateToJoinRoom(@NonNull NavController nc) {
        nc.navigate(MenuFragmentDirections.actionMenuFragmentToJoinRoomFragment());
    }

    public static void navigateToAdminRoom(@NonNull NavController nc, int roomID) {
        nc.navigate(CreateRoomFragmentDirections.actionCreateRoomFragmentToAdminRoomFragment(roomID));
    }

    public static void navigateToRegularRoom(@NonNull NavController nc, int roomID) {
        nc.navigate(JoinRoomFragmentDirections.actionJoinRoomFragmentToRegularRoomFragment(roomID));
    }

    public static void replaceFragment(@NonNull FragmentManager fragmentManager, @NonNull Fragment fragment, @IdRes int layout) {
        fragmentManager.beginTransaction().replace(layout, fragment).commit();
    }
}