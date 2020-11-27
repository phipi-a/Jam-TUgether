package de.pcps.jamtugether.api.responses;

// todo replace this with actual response
public class JoinRoomResponse {

    private final boolean roomExists;
    private final boolean passwordCorrect;

    public JoinRoomResponse(boolean roomExists, boolean passwordCorrect) {
        this.roomExists = roomExists;
        this.passwordCorrect = passwordCorrect;
    }

    public boolean roomExists() {
        return roomExists;
    }

    public boolean passwordCorrect() {
        return passwordCorrect;
    }
}
