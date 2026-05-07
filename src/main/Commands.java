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
      handleMove(command, game, player);
    } else if (command.equals("ende")) {
      return false;
    } else {
      System.out.println("Unbekannter Befehl. Tippe 'hilfe'.");
    }

    return true;
  }

  private static void handleMove(String command, Game game, Player player) {
    String direction = command.substring(5);
    Room nextRoom = player.getCurrentRoom().getExit(direction);

    if (nextRoom == null) {
      System.out.println("Dort ist kein Ausgang.");
      return;
    }

    if (nextRoom.getName().equals("Ausgang") && !player.hasKey()) {
      System.out.println(
        "Dieser Ausgang ist verschlossen. Ohne Schlüssel kommst du hier nicht weiter."
      );
      return;
    }

    if (nextRoom.getName().equals("Ausgang") && player.hasKey()) {
      game.getEscapedMessage();
    }

    player.setCurrentRoom(nextRoom);

    if (nextRoom.getName().equals("Foyer") && player.hasKey()) {
      game.getLastFoyerMessage();
    }

    if (nextRoom.getVisited()) {
      System.out.println(player.getCurrentRoom().getName());
    } else {
      System.out.println(player.getCurrentRoom().getDescription());
      System.out.println(player.getCurrentRoom().getInformation());
      if (nextRoom.getEvent() != null) {
        handle(nextRoom.getEvent().getCommand(), game);
      }
      nextRoom.setVisited(true);
    }
  }
}
