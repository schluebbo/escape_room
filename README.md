# EscapeCampus

EscapeCampus ist ein Java-Textadventure, das in einem verlassenen Campusgebäude spielt.
Der Spieler bewegt sich durch verschiedene Räume, sammelt Gegenstände und löst kleine Rätsel, um am Ende zu entkommen.

## Spielidee

Der Spieler startet im Foyer eines verlassenen Campusgebäudes.
Von dort aus kann er verschiedene Räume erkunden, Hinweise lesen und Gegenstände finden.
Durch einfache Texteingaben steuert der Spieler seine Bewegung und Aktionen.

## Steuerung

Folgende Befehle können im Spiel verwendet werden:

```text
hilfe
schau
info
karte
inventar
nimm sicherung
setze sicherung
gehe n
gehe s
gehe o
gehe w
gehe h
gehe r
ende
```

## Sicherungsrätsel

Im Spiel wurde ein Sicherungsrätsel eingebaut.

Im Flur liegt eine Sicherung auf dem Boden.
Die Tür zum Labor besitzt ein elektronisches Schloss und funktioniert erst, wenn der Notstrom wiederhergestellt wurde.

Der Spieler muss deshalb:

1. Vom Foyer in den Flur gehen
2. Die Sicherung aufnehmen
3. Die Sicherung in den Sicherungskasten einsetzen
4. Danach kann er das Labor betreten

Beispiel:

```text
gehe n
nimm sicherung
setze sicherung
gehe n
```

## Technische Umsetzung

Das Spiel wurde in Java entwickelt.

Für das Sicherungsrätsel wurden unter anderem folgende Dateien erweitert:

* `Player.java`
* `Commands.java`

In `Player.java` wird gespeichert, ob der Spieler die Sicherung besitzt und ob der Notstrom wiederhergestellt wurde.

In `Commands.java` wurden neue Befehle ergänzt und die Bewegung ins Labor wird blockiert, solange kein Notstrom vorhanden ist.

## Ziel des Features

Das Sicherungsrätsel sorgt dafür, dass der Spieler nicht einfach direkt weitergehen kann.
Er muss mit der Spielwelt interagieren und ein kleines Rätsel lösen.

Dadurch fühlt sich das Spiel mehr wie ein Escape-Room an.

## Projekt ausführen

Das Projekt kann in einer Java-Entwicklungsumgebung geöffnet und gestartet werden.

Voraussetzung:

* Java installiert
* IDE wie IntelliJ IDEA, Eclipse oder VS Code

## Teamarbeit mit GitHub

Das Projekt wird über GitHub verwaltet.
Dadurch können mehrere Personen gemeinsam am Code arbeiten, Änderungen hochladen und den aktuellen Stand des Projekts teilen.
