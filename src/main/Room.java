package main;

public class Room {

    // Name des Raums
    private String name;
    private boolean isVisited;

    // Beschreibung des Raums
    private String description;

    // Informationen des Raums
    private String information;

    // Nachbarräume in die vier Himmelsrichtungen
    private Room north;
    private Room south;
    private Room east;
    private Room west;
    private Room up;
    private Room down;

    /**
     * Konstruktor:
     * Ein Raum besitzt einen Namen und eine Beschreibung.
     */
    public Room(String name, String description, String information) {
        this.name = name;
        this.description = description;
        this.information = information;
    }

    /**
     * Gibt den Namen des Raums zurück.
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt die Beschreibung des Raums zurück.
     */
    public String getDescription() {
        return description;
    }

    /**
     *  Gibt die Informationen des Raums zurück
     */
    public String getInformation() {
        return information;
    }

    /**
     * Setzt alle Ausgänge auf einmal.
     * Diese Methode kann später nützlich sein.
     */
    public void setExits(Room north, Room south, Room east, Room west, Room up, Room down) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        this.up = up;
        this.down = down;
    }

    /**
     * Setzt genau einen Ausgang in einer Richtung.
     */
    public void setExit(String direction, Room room) {

        if (direction.equals("n")) {
            north = room;
        }

        if (direction.equals("s")) {
            south = room;
        }

        if (direction.equals("o")) {
            east = room;
        }

        if (direction.equals("w")) {
            west = room;
        }

        if (direction.equals("h")) {
            up = room;
        }

        if (direction.equals("r")) {
            down = room;
        }
    }

    public void setVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    public boolean getVisited() {
        return isVisited;
    }

    /**
     * Gibt den Nachbarraum in einer Richtung zurück.
     * Falls es dort keinen Ausgang gibt, wird null zurückgegeben.
     */
    public Room getExit(String direction) {

        if (direction.equals("n")) return north;
        if (direction.equals("s")) return south;
        if (direction.equals("o")) return east;
        if (direction.equals("w")) return west;
        if (direction.equals("h")) return up;
        if (direction.equals("r")) return down;

        return null;
    }
}
