package ui;

import model.Event;
import model.EventLog;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("console")) {
            new PantryApp();
        } else {
            new PantryAppGUI();
        }
        for (Event next : EventLog.getInstance()) {
            System.out.println(next.toString());
        }
    }
}
