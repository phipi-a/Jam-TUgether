package de.pcps.jamtugether.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class User implements Serializable {

    private final int id;

    @NonNull
    private final String name;

    public User(int id, @NonNull String name) {
        this.id = id;
        this.name = name;
    }

    public int getID() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }
}
