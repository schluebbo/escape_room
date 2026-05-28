package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    // Der eine Spieler des Spiels
    private Player player;

    // Steuert, ob die Spielschleife weiterläuft
    private boolean running;

    // Enthält alle Räume der aktuellen Welt
    private ArrayList<Room> rooms;

    /**
     * Konstruktor:
     * Ein Game soll immer in einem gültigen Zustand sein.
     * Deshalb gibt es von Anfang an mindestens einen Raum und einen Player.
     */
    public Game() {
        rooms = new ArrayList<>();

        // Vorläufige Minimalwelt
        rooms.add(new Room("Startraum", "Du bist in einem vorläufigen Startraum.", "Dies sind die Infos zum vorläufigem Startraum."));

        // Der Player wird sicher auf den ersten vorhandenen Raum gesetzt
        player = new Player(rooms.get(0));
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }

    /**
     * Startet das Spiel.
     */
    public void start() {

        // Versucht, die eigentliche Welt aus der CSV-Datei zu laden
        setupWorld();

        Scanner scanner = new Scanner(System.in);
        running = true;

        System.out.println("Willkommen bei EscapeCampus!");
        System.out.println("Tippe 'hilfe' für Befehle.");
        System.out.println(player.getCurrentRoom().getDescription());
        System.out.println(player.getCurrentRoom().getInformation());

        while (running) {
            System.out.print("> ");
            String command = scanner.nextLine();
            running = Commands.handle(command, player);
        }

        scanner.close();
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
     * Falls das Einlesen der Datei fehlschlägt, bleibt die Minimalwelt erhalten.
     */
    private void setupWorld() {
        String filePath = Paths.get("src", "campus.csv").toString();

        ArrayList<String> lines = readCsvFile(filePath);

        // Wenn keine Zeilen gelesen wurden, bleibt die vorhandene Welt bestehen
        if (lines.isEmpty()) {
            System.out.println("Die CSV-Datei konnte nicht gelesen werden.");
            System.out.println("Die vorläufige Welt bleibt erhalten.");
            return;
        }

        // Neue Raumliste vorbereiten
        ArrayList<Room> loadedRooms = new ArrayList<>();

        // Zuerst Räume anlegen
        createRoomsFromLines(lines, loadedRooms);

        // Dann Ausgänge setzen
        createExitsFromLines(lines, loadedRooms);

        // Danach Start-Raum setzen
        Room startRoom = findStartRoomFromLines(lines, loadedRooms);

        // Nur wenn ein Start-Raum gefunden wurde, wird die neue Welt übernommen
        if (startRoom != null) {
            rooms = loadedRooms;
            player.setCurrentRoom(startRoom);
            startRoom.setVisited(true);
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

                // Leere Zeilen werden übersprungen
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
    private void createRoomsFromLines(ArrayList<String> lines, ArrayList<Room> loadedRooms) {

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
     * <p>
     * Wichtige Vereinfachung:
     * Diese Methode setzt voraus, dass die Räume vorher bereits angelegt wurden.
     */
    private void createExitsFromLines(ArrayList<String> lines, ArrayList<Room> loadedRooms) {

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
    private Room findStartRoomFromLines(ArrayList<String> lines, ArrayList<Room> loadedRooms) {

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
}
