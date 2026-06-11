package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class World {

    // Enthält alle Räume der aktuellen Welt
    private ArrayList<Room> rooms;

    // Der Start-Raum der aktuellen Welt
    private Room startRoom;

    /**
     * Konstruktor:
     * Eine World soll immer gültig sein.
     * Deshalb gibt es zuerst eine Minimalwelt.
     */
    public World() {
        rooms = new ArrayList<>();

        Room defaultRoom = new Room(
                "Startraum",
                "Du bist in einem vorläufigen Startraum.",
                "Dies sind die Infos zum vorläufigem Startraum."
        );

        rooms.add(defaultRoom);
        startRoom = defaultRoom;
    }

    /**
     * Baut die eigentliche Spielwelt auf.
     * <p>
     * Idee:
     * 1. CSV-Datei komplett einlesen
     * 2. Räume anlegen
     * 3. Ausgänge anlegen
     * 4. Start-Raum setzen
     * <p>
     * Falls das Einlesen der Datei fehlschlägt,
     * bleibt die Minimalwelt erhalten.
     */
    public void setupWorld() {
        String filePath = Paths.get("src", "campus.csv").toString();

        ArrayList<String> lines = readCsvFile(filePath);

        if (lines.isEmpty()) {
            System.out.println("Die CSV-Datei konnte nicht gelesen werden.");
            System.out.println("Die vorläufige Welt bleibt erhalten.");
            return;
        }

        ArrayList<Room> loadedRooms = new ArrayList<>();

        createRoomsFromLines(lines, loadedRooms);
        createExitsFromLines(lines, loadedRooms);

        Room loadedStartRoom = findStartRoomFromLines(lines, loadedRooms);
        Room serverRoom = findRoom(loadedRooms, "Serverraum");

        if (loadedStartRoom != null && serverRoom != null) {
            rooms = loadedRooms;
            startRoom = loadedStartRoom;
            startRoom.setVisited(true);
            serverRoom.setEvent(new Event("key_found"));
        } else {
            System.out.println("Kein gültiger Start-Raum gefunden.");
            System.out.println("Die vorläufige Welt bleibt erhalten.");
        }
    }

    /**
     * Liest die CSV-Datei vollständig ein und speichert jede Zeile in einer ArrayList.
     */
    private ArrayList<String> readCsvFile(String fileName) {
        ArrayList<String> lines = new ArrayList<>();

        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (!line.equals("")) {
                    lines.add(line);
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Datei nicht gefunden: " + fileName);
        }

        return lines;
    }

    /**
     * Geht alle Zeilen durch und legt für jede ROOM-Zeile einen Raum an.
     */
    private void createRoomsFromLines(
            ArrayList<String> lines,
            ArrayList<Room> loadedRooms
    ) {
        for (String line : lines) {
            String[] parts = line.split(";");

            if (parts[0].trim().equals("ROOM")) {
                String name = parts[1].trim();
                String description = parts[2].replace(". ", ".\n");
                String information = parts[3].replace(". ", ".\n").replace(" '", "\n'");

                loadedRooms.add(new Room(name, description, information));
            }
        }
    }

    /**
     * Geht alle Zeilen durch und setzt für jede EXIT-Zeile einen Ausgang.
     */
    private void createExitsFromLines(
            ArrayList<String> lines,
            ArrayList<Room> loadedRooms
    ) {
        for (String line : lines) {
            String[] parts = line.split(";");

            if (parts[0].trim().equals("EXIT")) {
                String from = parts[1].trim();
                String direction = parts[2].trim();
                String to = parts[3].trim();

                Room fromRoom = findRoom(loadedRooms, from);
                Room toRoom = findRoom(loadedRooms, to);

                if (fromRoom != null && toRoom != null) {
                    fromRoom.setExit(direction, toRoom);
                }
            }
        }
    }

    /**
     * Sucht in den Zeilen nach STARTROOM und liefert den passenden Raum zurück.
     */
    private Room findStartRoomFromLines(
            ArrayList<String> lines,
            ArrayList<Room> loadedRooms
    ) {
        for (String line : lines) {
            String[] parts = line.split(";");

            if (parts[0].trim().equals("STARTROOM")) {
                String startRoomName = parts[1].trim();
                return findRoom(loadedRooms, startRoomName);
            }
        }

        return null;
    }

    /**
     * Gibt das Ende der Storyline aus,
     * sobald der Player mit dem Key ins Foyer kommt.
     */
    public void getLastFoyerMessage() {
        String filePath = Paths.get("src", "campus.csv").toString();
        ArrayList<String> lines = readCsvFile(filePath);

        for (String line : lines) {
            String[] parts = line.split(";");

            if (parts[0].trim().equals("OPENDOOR")) {
                String finalFoyerMessage = parts[1].trim();
                System.out.println(finalFoyerMessage);
            }
        }
    }

    /**
     * Gibt die finale Escape-Nachricht aus.
     */
    public void getEscapedMessage() {
        String filePath = Paths.get("src", "campus.csv").toString();
        ArrayList<String> lines = readCsvFile(filePath);

        for (String line : lines) {
            String[] parts = line.split(";");

            if (parts[0].trim().equals("FINALTEXT")) {
                String finalExitMessage = parts[1].trim();
                System.out.println(finalExitMessage);
            }
        }
    }

    /**
     * Sucht einen Raum mit einem bestimmten Namen in einer übergebenen Raumliste.
     */
    private Room findRoom(ArrayList<Room> roomList, String name) {
        for (Room room : roomList) {
            if (room.getName().equals(name)) {
                return room;
            }
        }

        return null;
    }

    public Room getStartRoom() {
        return startRoom;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }
}