package ui;

import model.Ingredient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// WHERE THE USE INGREDIENT DIALOGUE IS CREATED
public class UseIngredientDialogue implements ActionListener {

    private JFrame frame;
    private JDialog dialogue;
    private Ingredient ingredient;
    private JTextField input;
    JButton okButton;
    JButton cancelButton;

    // CONSTRUCTOR
    public UseIngredientDialogue(JFrame frame, Ingredient ingredient) {
        this.frame = frame;
        this.ingredient = ingredient;
    }

    // MODIFIES: this
    // EFFECTS: creates the dialogue when the use button was selected in the main ui
    public void showUseDialogue() {
        dialogue = new JDialog(frame, "Use Ingredient", true);
        dialogue.setLayout(new BoxLayout(dialogue.getContentPane(), BoxLayout.PAGE_AXIS));
        JPanel messagesPanel = setUpMessagesPanel();
        JPanel inputPanel = setUpInputPanel();
        JPanel buttonsPanel = setUpButtonsPanel();
        addComponentsToDialogue(messagesPanel, inputPanel, buttonsPanel);
        setUpDialogue();
        dialogue.setLocationRelativeTo(frame);
        dialogue.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: sizes the frame so that all contents inside the dialogue are at their preferred size
    private void setUpDialogue() {
        dialogue.pack();
        dialogue.setSize(new Dimension(400, 200));
        dialogue.setMaximumSize(new Dimension(400, 200));
        dialogue.setMinimumSize(new Dimension(400, 200));
    }

    // MODIFIES: this
    // EFFECTS: adds all necessary elements to the dialogue such as panels, glue etc.
    private void addComponentsToDialogue(JPanel messagesPanel, JPanel inputPanel, JPanel buttonsPanel) {
        dialogue.add(Box.createRigidArea(new Dimension(0, 20)));
        dialogue.add(messagesPanel);
        dialogue.add(Box.createRigidArea(new Dimension(0, 20)));
        dialogue.add(inputPanel);
        dialogue.add(Box.createVerticalGlue());
        dialogue.add(buttonsPanel);
    }

    // MODIFIES: this
    // EFFECTS: sets up the buttons and puts them into a panel ready to be used
    private JPanel setUpButtonsPanel() {
        okButton = new JButton("Ok");
        okButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);
        buttonsPanel.setMaximumSize(new Dimension(600, 50));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));
        return buttonsPanel;
    }

    // MODIFIES: this
    // EFFECTS: sets up the user input and puts it into a panel and returns it
    private JPanel setUpInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        JLabel amountLabel = new JLabel("Amount to use:");
        input = new JTextField(String.valueOf(ingredient.getAmount()), 20);
        inputPanel.add(amountLabel);
        inputPanel.add(input);
        inputPanel.setMaximumSize(new Dimension(400, 60));
        return inputPanel;
    }

    // MODIFIES: this
    // EFFECTS: sets up the various messages displayed in the use dialogue, puts it into a panel and returns it
    private JPanel setUpMessagesPanel() {
        JPanel messagesPanel = new JPanel(new GridLayout(2, 1));
        JLabel messageLine1 = new JLabel("How much of ingredient " + ingredient.getName() + " do you want to use?");
        JLabel messageLine2 = new JLabel("Currently the available amount is " + ingredient.getAmount());
        messagesPanel.add(messageLine1);
        messagesPanel.add(messageLine2);
        messagesPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        return messagesPanel;
    }

    // EFFECTS: will use the ingredient if ok it pressed, close dialogue if cancel is pressed
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == cancelButton) {
            dialogue.dispose();
        } else if (ae.getSource() == okButton) {
            int amount = -1;
            try {
                amount = Integer.valueOf(input.getText());
            } catch (NumberFormatException e) {
                System.out.println("Something has gone wrong");
            }

            if (amount > ingredient.getAmount() || amount <= 0) {
                input.setText(String.valueOf(ingredient.getAmount()));
                return;
            }
            ingredient.useIngredient(amount);
            dialogue.dispose();
        }
    }
}
