package main;

public class Commands {

  /**
   * Verarbeitet die Eingaben des Spielers.
   *
   * @return true, solange das Spiel weiterlaufen soll
   */
  public static boolean handle(String command, Game game) {
    Player player = game.getPlayer();
    if (command.equals("hilfe")) {
      System.out.println("Befehle: hilfe, schau, gehe n|s|o|w|h|r, ende");
    } else if (command.equals("schau")) {
      System.out.println(player.getCurrentRoom().getDescription());
    } else if (command.equals("info")) {
      System.out.println(player.getCurrentRoom().getInformation());
    } else if (command.startsWith("gehe ")) {
      String direction = command.substring(5);
      Room nextRoom = player.getCurrentRoom().getExit(direction);

      if (nextRoom.getName().equals("Ausgang") && !player.hasKey()) {
        System.out.println(
          "Dieser Ausgang ist verschlossen. Ohne Schlüssel kommst du hier nicht weiter."
        );
      }
      if (nextRoom.getName().equals("Foyer") && player.hasKey()) {
        game.getFinalMessage();
      }

      if (nextRoom == null) {
        System.out.println("Dort ist kein Ausgang.");
      } else {
        player.setCurrentRoom(nextRoom);
        nextRoom.setVisited(true);
        System.out.println(player.getCurrentRoom().getDescription());
        System.out.println(player.getCurrentRoom().getInformation());
      }
    } else if (command.equals("ende")) {
      return false;
    } else {
      System.out.println("Unbekannter Befehl. Tippe 'hilfe'.");
    }

    return true;
  }
}
