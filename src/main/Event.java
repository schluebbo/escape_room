package main;

public class Event {

    public Event(final String command) {
        this.command = command;
    }

    private String command;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
