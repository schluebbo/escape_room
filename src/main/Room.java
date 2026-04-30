package main;

public class Room {

    // Name des Raums
    private String name;

    // Beschreibung des Raums
    private String description;

    // Nachbarräume in die vier Himmelsrichtungen
    private Room north;
    private Room south;
    private Room east;
    private Room west;

    /**
     * Konstruktor:
     * Ein Raum besitzt einen Namen und eine Beschreibung.
     */
    public Room(String name, String description) {
        this.name = name;
        this.description = description;
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
     * Setzt alle Ausgänge auf einmal.
     * Diese Methode kann später nützlich sein.
     */
    public void setExits(Room north, Room south, Room east, Room west) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
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

        return null;
    }
}