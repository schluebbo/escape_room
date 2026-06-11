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
    int cellWidth = labelWidth(rooms);

    int minX = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxY = Integer.MIN_VALUE;

    Map<String, Room> roomAt = new HashMap<>();
    for (Map.Entry<Room, int[]> entry : positions.entrySet()) {
      int x = entry.getValue()[0];
      int y = entry.getValue()[1];
      minX = Math.min(minX, x);
      maxX = Math.max(maxX, x);
      minY = Math.min(minY, y);
      maxY = Math.max(maxY, y);
      roomAt.put(x + "," + y, entry.getKey());
    }

    for (int y = minY; y <= maxY; y++) {
      if (!rowHasRoom(y, minX, maxX, roomAt)) {
        continue;
      }

      String dropLine = renderDropBeforeRow(y, positions, minX, maxX, cellWidth);
      if (!dropLine.isEmpty()) {
        System.out.println(dropLine);
      }

      System.out.println(renderLabelRow(y, minX, maxX, roomAt, current, cellWidth));

      int nextRow = nextRoomRow(y, minX, maxX, roomAt);
      if (nextRow != -1) {
        String southLine = renderSouthLine(y, nextRow, minX, maxX, roomAt, cellWidth);
        if (!southLine.isEmpty()) {
          System.out.println(southLine);
        }
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

  private static String renderLabelRow(
    int y,
    int minX,
    int maxX,
    Map<String, Room> roomAt,
    Room current,
    int cellWidth
  ) {
    StringBuilder line = new StringBuilder();

    for (int x = minX; x <= maxX; x++) {
      Room room = roomAt.get(x + "," + y);
      line.append(padCenter(room == null ? "" : formatLabel(room, current), cellWidth));
      if (x < maxX) {
        Room east = roomAt.get((x + 1) + "," + y);
        line.append(
          room != null && east != null && connected(room, east) ? "-" : " "
        );
      }
    }

    return line.toString().stripTrailing();
  }

  private static String renderSouthLine(
    int y,
    int nextY,
    int minX,
    int maxX,
    Map<String, Room> roomAt,
    int cellWidth
  ) {
    StringBuilder line = new StringBuilder();
    boolean hasConnector = false;

    for (int x = minX; x <= maxX; x++) {
      Room room = roomAt.get(x + "," + y);
      Room south = roomAt.get(x + "," + nextY);
      if (room != null && south != null && connected(room, south)) {
        line.append(padCenter("|", cellWidth));
        hasConnector = true;
      } else {
        line.append(spaces(cellWidth));
      }
      if (x < maxX) {
        line.append(" ");
      }
    }

    return hasConnector ? line.toString().stripTrailing() : "";
  }

  private static String renderDropBeforeRow(
    int y,
    Map<Room, int[]> positions,
    int minX,
    int maxX,
    int cellWidth
  ) {
    for (Map.Entry<Room, int[]> entry : positions.entrySet()) {
      if (entry.getValue()[1] != y) {
        continue;
      }

      Room upper = entry.getKey().getExit("h");
      if (upper == null) {
        continue;
      }

      int x = positions.get(upper)[0];
      StringBuilder line = new StringBuilder();
      for (int col = minX; col <= maxX; col++) {
        if (col == x) {
          line.append(padCenter("↓", cellWidth));
        } else {
          line.append(spaces(cellWidth));
        }
        if (col < maxX) {
          line.append(" ");
        }
      }
      return line.toString().stripTrailing();
    }

    return "";
  }

  private static boolean rowHasRoom(
    int y,
    int minX,
    int maxX,
    Map<String, Room> roomAt
  ) {
    for (int x = minX; x <= maxX; x++) {
      if (roomAt.get(x + "," + y) != null) {
        return true;
      }
    }
    return false;
  }

  private static int nextRoomRow(
    int y,
    int minX,
    int maxX,
    Map<String, Room> roomAt
  ) {
    for (int next = y + 1; next <= maxRowFromMap(roomAt); next++) {
      if (rowHasRoom(next, minX, maxX, roomAt)) {
        return next;
      }
    }
    return -1;
  }

  private static int maxRowFromMap(Map<String, Room> roomAt) {
    int max = 0;
    for (String key : roomAt.keySet()) {
      max = Math.max(max, Integer.parseInt(key.split(",")[1]));
    }
    return max;
  }

  private static int labelWidth(ArrayList<Room> rooms) {
    int width = 3;
    for (Room room : rooms) {
      width = Math.max(width, room.getName().length() + 2);
    }
    return width;
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

  private static String padCenter(String text, int width) {
    if (text.length() >= width) {
      return text;
    }
    int left = (width - text.length()) / 2;
    return spaces(left) + text + spaces(width - text.length() - left);
  }

  private static String spaces(int count) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < count; i++) {
      builder.append(' ');
    }
    return builder.toString();
  }

  private static int maxRow(Map<Room, int[]> positions) {
    int max = 0;
    for (int[] pos : positions.values()) {
      max = Math.max(max, pos[1]);
    }
    return max;
  }
}
