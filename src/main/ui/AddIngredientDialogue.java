package ui;

import model.FoodCategories;
import model.Ingredient;
import model.Pantry;
import model.Purchase;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// WHERE THE ADD INGREDIENT DIALOGUE IS CREATED
public class AddIngredientDialogue implements ActionListener {

    private JFrame frame;
    private Pantry pantry;
    private Ingredient ingredient;
    private String category;
    private JDialog dialogue;
    private JComboBox<String> categoryComboBox;
    private JTextField nameTextField;
    private JCheckBox liquidCheckBox;
    private JCheckBox individualCheckBox;
    private JTextField amountTextField;
    private JTextField dateLabTextField;
    private JButton okButton;
    private JButton cancelButton;

    // CONSTRUCTOR
    public AddIngredientDialogue(JFrame frame, Pantry pantry, Ingredient ingredient, String category) {
        this.frame = frame;
        this.pantry = pantry;
        this.ingredient = ingredient;
        this.category = category;
    }

    // EFFECTS: initializes the ui for the add ingredient dialogue
    public void showAddDialogue() {
        dialogue = new JDialog(frame, "Add Ingredient", true);
        dialogue.setLayout(new BoxLayout(dialogue.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 0));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(30, -10, 30, 90));

        JLabel categoryLabel = new JLabel("Category:");
        categoryLabel.setHorizontalAlignment(JLabel.RIGHT);
        DefaultComboBoxModel<String> pantryCategories = new DefaultComboBoxModel<>();
        for (FoodCategories c : FoodCategories.values()) {
            pantryCategories.addElement(c.toString());
        }

        initializeCategoryComboBox(inputPanel, categoryLabel, pantryCategories);
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setHorizontalAlignment(JLabel.RIGHT);
        initializeNameTextField(inputPanel, nameLabel);

        JLabel emptyLabel1 = new JLabel("");
        emptyLabel1.setHorizontalAlignment(JLabel.RIGHT);
        JLabel emptyLabel2 = new JLabel("");
        emptyLabel2.setHorizontalAlignment(JLabel.RIGHT);

        initializeLiquidAndIndividualCheck(inputPanel, emptyLabel1, emptyLabel2);
        createAmount(inputPanel);
        createDate(inputPanel);

        JPanel buttonsPanel = initializeOkAndCancel();
        finalizeDialogue(inputPanel, buttonsPanel);
    }

    // MODIFIES: this
    // EFFECTS: creates label for the date
    private void createDate(JPanel inputPanel) {
        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setHorizontalAlignment(JLabel.RIGHT);
        dateLabTextField = new JTextField();
        inputPanel.add(dateLabel);
        inputPanel.add(dateLabTextField);
    }

    // MODIFIES: this
    // EFFECTS: creates label for the amount
    private void createAmount(JPanel inputPanel) {
        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setHorizontalAlignment(JLabel.RIGHT);
        amountTextField = new JTextField();
        inputPanel.add(amountLabel);
        inputPanel.add(amountTextField);
    }

    // MODIFIES: this
    // EFFECTS: creates combo box to display categories
    private void initializeCategoryComboBox(JPanel inputPanel, JLabel categoryLabel,
                                            DefaultComboBoxModel<String> pantryCategories) {
        categoryComboBox = new JComboBox<>(pantryCategories);
        categoryComboBox.setSelectedIndex(-1);
        if (category != null) {
            categoryComboBox.setSelectedItem(category);
        }
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryComboBox);
    }

    // MODIFIES: this
    // EFFECTS: adds to the inputPanel to allow users to add the name of item they are adding
    private void initializeNameTextField(JPanel inputPanel, JLabel nameLabel) {
        nameTextField = new JTextField();
        if (ingredient != null) {
            nameTextField.setText(ingredient.getName());
        }
        inputPanel.add(nameLabel);
        inputPanel.add(nameTextField);
    }

    // MODIFIES: this
    // EFFECTS: creates the checkboxes for if the item is liquid or individually measured
    private void initializeLiquidAndIndividualCheck(JPanel inputPanel, JLabel emptyLabel1, JLabel emptyLabel2) {
        liquidCheckBox = new JCheckBox("Liquid");
        individualCheckBox = new JCheckBox("Individual");
        inputPanel.add(emptyLabel1);
        inputPanel.add(liquidCheckBox);
        inputPanel.add(emptyLabel2);
        inputPanel.add(individualCheckBox);
    }

    // MODIFIES: this
    // EFFECTS: creates the ok and cancel buttons
    private JPanel initializeOkAndCancel() {
        okButton = new JButton("Ok");
        okButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        JPanel buttonsPanel = createButtonPanel();
        return buttonsPanel;
    }

    // EFFECTS: creates button panel to house the buttons
    private JPanel createButtonPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(Box.createHorizontalGlue());
        buttonsPanel.add(okButton);
        buttonsPanel.add(cancelButton);
        buttonsPanel.setMaximumSize(new Dimension(600, 50));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));
        return buttonsPanel;
    }

    // EFFECTS: puts together all the information previously initialized to finalize the dialogue field
    private void finalizeDialogue(JPanel inputPanel, JPanel buttonsPanel) {
        dialogue.add(inputPanel);
        dialogue.add(buttonsPanel);

        dialogue.pack();

        dialogue.setMaximumSize(new Dimension(410, 300));
        dialogue.setMinimumSize(new Dimension(410, 300));
        dialogue.setLocationRelativeTo(frame);
        dialogue.setVisible(true);
    }

    // EFFECTS: call functions dependent on the action event
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == okButton) {
            okButtonDialogue();
        } else if (ae.getSource() == cancelButton) {
            dialogue.dispose();
        }
    }

    // MODIFIES: this
    // EFFECTS: adds the Ingredient after ok button is pressed
    public void okButtonDialogue() {
        String c = (String) categoryComboBox.getSelectedItem();
        if (c == null) {
            return;
        }
        FoodCategories category = FoodCategories.valueOf(c);

        String name = nameTextField.getText().strip();
        if (name.isEmpty()) {
            return;
        }

        boolean isLiquid = liquidCheckBox.isSelected();
        boolean isIndividual = individualCheckBox.isSelected();

        String a = amountTextField.getText().strip();
        Integer amount = getAmount(a);
        if (amount == null) {
            return;
        }

        String d = dateLabTextField.getText();
        LocalDate date = getDate(d);
        if (date == null) {
            return;
        }
        createIngredientToAdd(category, name, isLiquid, isIndividual, amount, date);
        dialogue.dispose();
    }

    // MODIFIES: this
    // EFFECTS: adds purchase to ingredient in list
    private void createIngredientToAdd(FoodCategories category, String name, boolean isLiquid,
                                       boolean isIndividual, Integer amount, LocalDate date) {
        Ingredient ingredientToAdd = createIngredient(category, name, isLiquid, isIndividual);

        Purchase purchase = new Purchase(amount, date);
        ingredientToAdd.addPurchase(purchase);
    }

    // EFFECTS: converts string to date
    private static LocalDate getDate(String d) {
        LocalDate date = LocalDate.of(1, 1, 1);
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            date = LocalDate.parse(d, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
        return date;
    }

    // EFFECTS: returns amount inputted as an int
    private static Integer getAmount(String a) {
        int amount = 0;
        try {
            amount = Integer.valueOf(a);
        } catch (NumberFormatException ex) {
            return null;
        }
        return amount;
    }

    // EFFECTS: creates an ingredient we want to add to and returns it
    private Ingredient createIngredient(FoodCategories category, String name, boolean isLiquid, boolean isIndividual) {
        Ingredient ingredientToAdd = pantry.getFoodByName(name);
        if (ingredientToAdd == null) {
            ingredientToAdd = new Ingredient(name, category, isLiquid, isIndividual);
            pantry.addIngredientList(ingredientToAdd);
        }
        return ingredientToAdd;
    }
}
