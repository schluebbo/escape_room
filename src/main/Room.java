package main;

public class Room {

    // Name des Raums
    private final String name;

    // Beschreibung des Raums
    private final String description;

    // Ob der Raum bereits besucht wurde
    private boolean isChecked;

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
    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        this.isChecked = false;
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

    /**
     * Gibt den Nachbarraum in einer Richtung zurück.
     * Falls es dort keinen Ausgang gibt, wird null zurückgegeben.
     */
    public Room getExit(String direction) {
        return switch (direction) {
            case "n" -> north;
            case "s" -> south;
            case "o" -> east;
            case "w" -> west;
            case "h" -> up;
            case "r" -> down;
            default -> null;
        };
    }

    /**
     * Gibt zurück, ob der Raum bereits besucht wurde.
     */
    public boolean isChecked() {
        return isChecked;
    }

    /**
     * Markiert den Raum als besucht.
     */
    public void markChecked() {
        isChecked = true;
    }
}