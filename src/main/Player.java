package main;

public class Player {

    // Der Raum, in dem sich der Spieler aktuell befindet
    private Room currentRoom;

    /**
     * Konstruktor:
     * Der Player startet in einem übergebenen Raum.
     */
    public Player(Room startRoom) {
        setCurrentRoom(startRoom);
    }

    /**
     * Gibt den aktuellen Raum des Spielers zurück.
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Setzt den aktuellen Raum des Spielers.
     */
    public void setCurrentRoom(Room room) {
        currentRoom = room;
        currentRoom.markChecked();
    }
}