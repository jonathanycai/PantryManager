package persistence;

import model.Pantry;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class JSonWriter {
    private PrintWriter printWriter;
    private String destination;
    private static final int indentFactor = 3;


    // represents a writer which writes a Pantry object into a json file
    public JSonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: starts the writing process by creating a new PrintWriter object; throws
    // FileNotFoundException if it cannot be opened
    public void open() throws FileNotFoundException {
        printWriter = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes json representation of the pantry to file
    public void write(Pantry pantry) {
        JSONObject jsonObject = new JSONObject();
        jsonObject = pantry.toJson();
        saveObjToFile(jsonObject.toString(indentFactor));
    }

    // MODIFIES: this
    // EFFECTS: saves string to the file
    public void saveObjToFile(String jsonObj) {
        printWriter.println(jsonObj);
    }

    // MODIFIES: this
    // EFFECTS: closes the file
    public void close() {
        printWriter.close();
    }
}
