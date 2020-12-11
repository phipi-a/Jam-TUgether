package de.pcps.jamtugether.models.music.volume;

public class Volume {

    private final int roomID;
    private final int userID;
    private final int volume;

    public Volume(int roomID, int userID, int volume) {
        this.roomID = roomID;
        this.userID = userID;
        this.volume = volume;
    }

    public int getRoomID() {
        return roomID;
    }

    public int getUserID() {
        return userID;
    }

    public int getVolume() {
        return volume;
    }
}
