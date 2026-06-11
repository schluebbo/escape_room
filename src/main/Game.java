package main;

import java.util.Scanner;

public class Game {

    // Der eine Spieler des Spiels
    private Player player;

    // Die Welt des Spiels
    private World world;

    // Steuert, ob die Spielschleife weiterläuft
    private boolean running;

    /**
     * Konstruktor:
     * Game erstellt eine World und setzt den Player in den Start-Raum.
     */
    public Game() {
        world = new World();
        player = new Player(world.getStartRoom());
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
        world.setupWorld();

        // Player auf den Start-Raum der geladenen Welt setzen
        player.setCurrentRoom(world.getStartRoom());

        Scanner scanner = new Scanner(System.in);
        running = true;

        System.out.println("Willkommen bei EscapeCampus!");
        System.out.println("Tippe 'hilfe' für Befehle.");
        System.out.println(player.getCurrentRoom().getDescription());
        System.out.println(player.getCurrentRoom().getInformation());

        while (running) {
            System.out.print("> ");
            String command = scanner.nextLine();
            running = Commands.handle(command, this);
        }

        scanner.close();
    }

    public Player getPlayer() {
        return this.player;
    }

    public World getWorld() {
        return this.world;
    }

    /**
     * Diese Methoden bleiben hier als Weiterleitung,
     * damit Commands nicht unbedingt geändert werden muss.
     */
    public void getLastFoyerMessage() {
        world.getLastFoyerMessage();
    }

    public void getEscapedMessage() {
        world.getEscapedMessage();
    }
}