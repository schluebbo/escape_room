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
      System.out.println(
        "Befehle: hilfe, schau, info, karte, inventar, nimm sicherung, setze sicherung, gehe n|s|o|w|h|r, ende"
      );
    } else if (command.equals("karte")) {
      CampusMap.print(game);
    } else if (command.equals("schau")) {
      System.out.println(player.getCurrentRoom().getDescription());
    } else if (command.equals("info")) {
      System.out.println(player.getCurrentRoom().getInformation());
    } else if (command.equals("inventar")) {
      printInventory(player);
    } else if (command.equals("nimm sicherung")) {
      takeFuse(player);
    } else if (command.equals("setze sicherung")) {
      insertFuse(player);
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

    if (
      player.getCurrentRoom().getName().equals("Flur")
        && nextRoom.getName().equals("Labor")
        && !player.hasPowerRestored()
    ) {
      System.out.println(
        "Die Labortür reagiert nicht. Ohne Notstrom bleibt das elektronische Schloss tot."
      );
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
        handleEvent(nextRoom.getEvent().getCommand(), player);
      }
      nextRoom.setVisited(true);
    }
  }

  private static void handleEvent(String command, Player player) {
    if (command.equals("key_found")) {
      player.setKey(true);
    }
  }

  private static void takeFuse(Player player) {
    if (player.hasPowerRestored()) {
      System.out.println("Die Sicherung steckt bereits im Sicherungskasten.");
      return;
    }

    if (player.hasFuse()) {
      System.out.println("Du hast die Sicherung bereits aufgenommen.");
      return;
    }

    if (!player.getCurrentRoom().getName().equals("Flur")) {
      System.out.println("Hier liegt keine Sicherung.");
      return;
    }

    player.setFuse(true);
    System.out.println("Du nimmst die Sicherung auf.");
  }

  private static void insertFuse(Player player) {
    if (!player.getCurrentRoom().getName().equals("Flur")) {
      System.out.println("Hier gibt es keinen offenen Sicherungskasten.");
      return;
    }

    if (player.hasPowerRestored()) {
      System.out.println("Der Notstrom ist bereits wiederhergestellt.");
      return;
    }

    if (!player.hasFuse()) {
      System.out.println("Dir fehlt die Sicherung.");
      return;
    }

    player.setFuse(false);
    player.setPowerRestored(true);
    System.out.println("Du setzt die Sicherung ein. Der Notstrom ist wiederhergestellt.");
  }

  private static void printInventory(Player player) {
    System.out.println("Inventar:");
    if(player.hasFuse()) {
      System.out.println("- Sicherung: ja");
    } else {
      System.out.println("- Sicherung: nein");
    }
    if(player.hasKey()) {
      System.out.println("- Schlüssel: ja");
    } else {
      System.out.println("- Schlüssel: nein");
    }
  }
}
