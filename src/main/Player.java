package main;

public class Player {

    // Der Raum, in dem sich der Spieler aktuell befindet
    private Room currentRoom;

    // Der Schlüssel für den letzten Raum
    private boolean key;

    // Die Sicherung für den Sicherungskasten im Flur
    private boolean fuse;

    // Gibt an, ob der Notstrom wiederhergestellt wurde
    private boolean powerRestored;

    /**
     * Konstruktor:
     * Der Player startet in einem übergebenen Raum.
     */
    public Player(Room startRoom) {
        currentRoom = startRoom;
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
    }

    public boolean hasKey() {
        return key;
    }

    public void setKey(boolean hasKey) {
        key = hasKey;
    }

    public boolean hasFuse() {
        return fuse;
    }

    public void setFuse(boolean hasFuse) {
        fuse = hasFuse;
    }

    public boolean hasPowerRestored() {
        return powerRestored;
    }

    public void setPowerRestored(boolean hasPowerRestored) {
        powerRestored = hasPowerRestored;
    }
}