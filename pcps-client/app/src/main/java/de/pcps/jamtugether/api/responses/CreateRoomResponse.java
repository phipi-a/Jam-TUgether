package de.pcps.jamtugether.api.responses;

// todo replace with actual response
public class CreateRoomResponse {

    private final int roomID;
    private final String token;

    public CreateRoomResponse(int roomID, String token) {
        this.roomID = roomID;
        this.token = token;
    }

    public int getRoomID() {
        return roomID;
    }

    public String getToken() {
        return token;
    }
}
