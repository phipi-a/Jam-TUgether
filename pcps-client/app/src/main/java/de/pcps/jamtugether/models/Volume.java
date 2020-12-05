package de.pcps.jamtugether.models;

public class Volume {

    private final int room;
    private final int user;
    private final int volume;

    public Volume(int room, int user, int volume) {
        this.room = room;
        this.user = user;
        this.volume = volume;
    }

    public int getRoom() {
        return room;
    }

    public int getUser() {
        return user;
    }

    public int getVolume() {
        return volume;
    }
}
