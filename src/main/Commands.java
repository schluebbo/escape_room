package main;

public class Commands {

    /**
     * Verarbeitet die Eingaben des Spielers.
     *
     * @return true, solange das Spiel weiterlaufen soll
     */
    public static boolean handle(String command, Player player) {

        if (command.equals("hilfe")) {
            System.out.println("Befehle: hilfe, schau, gehe n|s|o|w|h|r, ende");
        } else if (command.equals("schau")) {
            System.out.println(player.getCurrentRoom().getDescription());
        } else if (command.equals("info")) {
            System.out.println(player.getCurrentRoom().getInformation());
        } else if (command.startsWith("gehe ")) {

            String direction = command.substring(5);
            Room nextRoom = player.getCurrentRoom().getExit(direction);

            if (nextRoom == null) {
                System.out.println("Dort ist kein Ausgang.");
            } else {
                player.setCurrentRoom(nextRoom);
                if (nextRoom.getVisited()){
                    System.out.println(player.getCurrentRoom().getName());
                } else {
                    System.out.println(player.getCurrentRoom().getDescription());
                    System.out.println(player.getCurrentRoom().getInformation());
                    nextRoom.setVisited(true);
                }

            }
        } else if (command.equals("ende")) {
            return false;
        } else {
            System.out.println("Unbekannter Befehl. Tippe 'hilfe'.");
        }

        return true;
    }
}
