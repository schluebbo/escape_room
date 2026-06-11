package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class CampusMap {

  private static final String[] DIRECTIONS = {"n", "s", "o", "w"};
  private static final int[][] DELTAS = {{0, -1}, {0, 1}, {1, 0}, {-1, 0}};

  public static void print(Game game) {
    ArrayList<Room> rooms = game.getWorld().getRooms();
    if (rooms.isEmpty()) {
      System.out.println("Keine Karte für diese Welt verfügbar.");
      return;
    }

    Room current = game.getPlayer().getCurrentRoom();
    Map<Room, int[]> positions = layout(rooms);

    int minY = Integer.MAX_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (int[] pos : positions.values()) {
      minY = Math.min(minY, pos[1]);
      maxY = Math.max(maxY, pos[1]);
    }

    for (int y = minY; y <= maxY; y++) {
      ArrayList<Room> row = new ArrayList<>();
      for (Map.Entry<Room, int[]> entry : positions.entrySet()) {
        if (entry.getValue()[1] == y) {
          row.add(entry.getKey());
        }
      }
      row.sort((a, b) -> positions.get(a)[0] - positions.get(b)[0]);
      if (!row.isEmpty()) {
        System.out.println(renderRow(row, current));
      }
    }

    for (Room room : rooms) {
      Room lower = room.getExit("r");
      if (lower != null) {
        System.out.println();
        System.out.println(formatLabel(room, current) + " ↓ " + formatLabel(lower, current));
        break;
      }
    }

    int visited = 0;
    for (Room room : rooms) {
      if (room.getVisited()) {
        visited++;
      }
    }

    System.out.println();
    System.out.println(
      "Besucht: "
        + visited
        + "/"
        + rooms.size()
        + " Räume | Schlüssel: "
        + (game.getPlayer().hasKey() ? "ja" : "nein")
    );
  }

  private static Map<Room, int[]> layout(ArrayList<Room> rooms) {
    Map<Room, int[]> positions = new HashMap<>();
    Set<String> occupied = new HashSet<>();
    Set<Room> placed = new HashSet<>();

    expand(rooms.get(0), 0, 0, positions, occupied, placed);

    int basementRow = maxRow(positions) + 2;
    for (Room room : rooms) {
      Room lower = room.getExit("r");
      if (lower != null && !placed.contains(lower)) {
        expand(lower, positions.get(room)[0], basementRow, positions, occupied, placed);
      }
    }

    int col = 0;
    int orphanRow = maxRow(positions) + 2;
    for (Room room : rooms) {
      if (!placed.contains(room)) {
        expand(room, col++, orphanRow, positions, occupied, placed);
      }
    }

    return positions;
  }

  private static void expand(
    Room start,
    int x,
    int y,
    Map<Room, int[]> positions,
    Set<String> occupied,
    Set<Room> placed
  ) {
    Queue<Room> queue = new LinkedList<>();
    queue.add(start);
    positions.put(start, freeSpot(x, y, occupied));
    placed.add(start);

    while (!queue.isEmpty()) {
      Room room = queue.poll();
      int[] pos = positions.get(room);

      for (int i = 0; i < DIRECTIONS.length; i++) {
        Room neighbor = room.getExit(DIRECTIONS[i]);
        if (neighbor == null || placed.contains(neighbor)) {
          continue;
        }

        positions.put(
          neighbor,
          freeSpot(pos[0] + DELTAS[i][0], pos[1] + DELTAS[i][1], occupied)
        );
        placed.add(neighbor);
        queue.add(neighbor);
      }
    }
  }

  private static int[] freeSpot(int x, int y, Set<String> occupied) {
    while (occupied.contains(x + "," + y)) {
      y++;
    }
    occupied.add(x + "," + y);
    return new int[] {x, y};
  }

  private static String renderRow(ArrayList<Room> row, Room current) {
    StringBuilder line = new StringBuilder();
    Room previous = null;

    for (Room room : row) {
      if (previous != null) {
        line.append(connected(previous, room) ? " - " : "   ");
      }
      line.append(formatLabel(room, current));
      previous = room;
    }

    return line.toString();
  }

  private static boolean connected(Room first, Room second) {
    return exitsTo(first, second) || exitsTo(second, first);
  }

  private static boolean exitsTo(Room from, Room to) {
    for (String direction : DIRECTIONS) {
      if (from.getExit(direction) == to) {
        return true;
      }
    }
    return false;
  }

  private static String formatLabel(Room room, Room current) {
    if (room == current) {
      return "[*" + room.getName() + "*]";
    }
    if (!room.getVisited()) {
      return "[?]";
    }
    return "[" + room.getName() + "]";
  }

  private static int maxRow(Map<Room, int[]> positions) {
    int max = 0;
    for (int[] pos : positions.values()) {
      max = Math.max(max, pos[1]);
    }
    return max;
  }
}
