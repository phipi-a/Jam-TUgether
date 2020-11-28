package de.pcps.jamtugether.api.responses;

// todo replace with actual response
public class CreateRoomResponse {

    private final int roomID;

    public CreateRoomResponse(int roomID) {
        this.roomID = roomID;
    }

    public int getRoomID() {
        return roomID;
    }
}
