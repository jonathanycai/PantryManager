package ui;

import model.Pantry;

import javax.swing.*;

public class PantryAppGUI {

    private Pantry pantry;

    // CONSTRUCTOR which calls runPantryGUI
    public PantryAppGUI() {
        runPantryGui();
    }

    // EFFECTS: allows us to safely call the methods in the swing class
    private void runPantryGui() {
        SwingUtilities.invokeLater(this::buildNewPantryUI);
    }

    // EFFECTS: runs the pantry ui
    private void buildNewPantryUI() {
        MainPantryUI mainUI = new MainPantryUI(pantry);
        mainUI.setVisible(true);
    }
}
