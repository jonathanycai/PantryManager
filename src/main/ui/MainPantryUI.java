package ui;

import model.*;
import model.Event;
import persistence.JSonReader;
import persistence.JSonWriter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

// WHERE THE MAIN UI GETS BUILT (HOME SCREEN)
public class MainPantryUI extends JFrame implements ActionListener, ListSelectionListener, WindowListener {

    private Pantry pantry;
    JButton loadButton;
    JButton saveButton;
    private JComboBox<String> categoryCombo;
    private JList<Ingredient> ingredientList;
    private JLabel amountValueLabel;
    private JLabel expirationLabel;
    JButton addButton;
    JButton useButton;
    JLabel picLabel;

    // EFFECTS: Constructor
    public MainPantryUI(Pantry pantry) {
        this.pantry = pantry;
        this.setMinimumSize(new Dimension(400, 580));
        generateUI();
    }

    // MODIFIES: this
    // EFFECTS: The main function to create the ui including buttons and panels
    public void generateUI() {
        addWindowListener(this);
        JPanel loadSavePanel = createLoadSavePanel();
        JLabel categoryLabel = createCategory();
        showIngredient();
        JScrollPane scrollPane = new JScrollPane(ingredientList);
        JPanel summaryPanel = createSummaryPanel();
        JPanel summaryAmount = createSummaryAmount();
        JPanel summaryExpiry = createSummaryExpiration();
        JPanel picPanel = createPicturePanel();
        JPanel buttonsPanel = createButtons();
        combinePanels(summaryPanel, summaryAmount, summaryExpiry, picPanel, buttonsPanel);
        setUpLayout(loadSavePanel, categoryLabel, scrollPane, summaryPanel);
        pack();
        setTitle("My Pantry");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
    }

    // MODIFIES: this
    // EFFECTS: creates the panel for the expiration label in the Summary panel
    public JPanel createSummaryExpiration() {
        JPanel summaryExpiry = new JPanel();
        summaryExpiry.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel expiryLabel = new JLabel("Expired: ");
        expirationLabel = new JLabel("");
        summaryExpiry.add(expiryLabel);
        summaryExpiry.add(expirationLabel);
        summaryExpiry.setMaximumSize(new Dimension(110, 20));
        return summaryExpiry;
    }

    // MODIFIES: this
    // EFFECTS: creates the panel for the amount label in the Summary panel
    public JPanel createSummaryAmount() {
        JPanel summaryAmount = new JPanel();
        summaryAmount.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel amountLabel = new JLabel("Amount: ");
        amountValueLabel = new JLabel("");
        summaryAmount.add(amountLabel);
        summaryAmount.add(amountValueLabel);
        summaryAmount.setMaximumSize(new Dimension(110, 20));
        return summaryAmount;
    }

    // MODIFIES: this
    // EFFECTS: creates the add and use buttons and puts them into a panel and returns them
    public JPanel createButtons() {
        addButton = new JButton();
        addButton.setText("Add");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.setEnabled(false);
        addButton.addActionListener(this);
        useButton = new JButton();
        useButton.setText("Use");
        useButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        useButton.setEnabled(false);
        useButton.addActionListener(this);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(2, 1));
        buttonsPanel.setMaximumSize(new Dimension(110, 200));
        buttonsPanel.add(addButton);
        buttonsPanel.add(useButton);
        return buttonsPanel;
    }

    // MODIFIES: this
    // EFFECTS: creates the layout of the ui and puts it all together
    public void setUpLayout(JPanel loadSavePanel, JLabel categoryLabel, JScrollPane scrollPane, JPanel summaryPanel) {
        var pane = getContentPane();
        var layout = new GroupLayout(pane);
        pane.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        setUpHorizontalGroup(loadSavePanel, categoryLabel, scrollPane, summaryPanel, layout);
        setUpVerticalGroup(loadSavePanel, categoryLabel, scrollPane, summaryPanel, layout);
    }

    // MODIFIES: this
    // EFFECTS: creates the vertical group in the ui
    public void setUpVerticalGroup(JPanel loadSavePanel, JLabel categoryLabel, JScrollPane scrollPane,
                                    JPanel summaryPanel, GroupLayout layout) {
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(loadSavePanel)
                                .addComponent(categoryLabel)
                                .addComponent(categoryCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(scrollPane)
                                .addComponent(summaryPanel)));
    }

    // MODIFIES: this
    // EFFECTS: creates the horizontal group in the ui
    public void setUpHorizontalGroup(JPanel loadSavePanel, JLabel categoryLabel, JScrollPane scrollPane,
                                      JPanel summaryPanel, GroupLayout layout) {
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(loadSavePanel)
                                .addComponent(categoryLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(categoryCombo))
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(scrollPane)
                                .addComponent(summaryPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)));
    }

    // MODIFIES: this
    // EFFECTS: puts together all the summary panels and returns it
    public static void combinePanels(JPanel summaryPanel, JPanel summaryAmount, JPanel summaryExpiry,
                                      JPanel picPanel, JPanel buttonsPanel) {
        summaryPanel.add(summaryAmount);
        summaryPanel.add(summaryExpiry);
        summaryPanel.add(picPanel);
        summaryPanel.add(Box.createVerticalGlue());
        summaryPanel.add(buttonsPanel);
    }

    // EFFECTS: creates a panel for the picture and returns it
    public JPanel createPicturePanel() {
        JPanel picPanel = new JPanel();
        picLabel = new JLabel();
        picPanel.add(picLabel);
        return picPanel;
    }

    // EFFECTS: creates a template for the summary panel and returns it
    public static JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel();
        summaryPanel.setName("Summary");
        summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Summary: "));
        summaryPanel.setPreferredSize(new Dimension(120, 0));
        summaryPanel.setMaximumSize(new Dimension(120, Short.MAX_VALUE));
        return summaryPanel;
    }

    // MODIFIES: this
    // EFFECTS: creates the list which helps view the ingredients
    public void showIngredient() {
        DefaultListModel<Ingredient> listModel = new DefaultListModel<>();
        ingredientList = new JList<>(listModel);
        ingredientList.setLayoutOrientation(JList.VERTICAL);
        ingredientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ingredientList.setFont(
                new Font(ingredientList.getFont().getName(), Font.PLAIN, ingredientList.getFont().getSize() + 2));
        ingredientList.addListSelectionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: creates the part of the ui which helps display the categories
    public JLabel createCategory() {
        JLabel categoryLabel = new JLabel("Choose Category: ", JLabel.RIGHT);
        DefaultComboBoxModel<String> pantryCategories = new DefaultComboBoxModel<>();
        for (FoodCategories c : FoodCategories.values()) {
            pantryCategories.addElement(c.toString());
        }
        categoryCombo = new JComboBox<>(pantryCategories);
        categoryCombo.setSelectedIndex(-1);
        categoryCombo.addActionListener(this);
        categoryCombo.setEnabled(false);
        return categoryLabel;
    }

    // MODIFIES: this
    // EFFECTS: creates a panel for the load and save buttons
    public JPanel createLoadSavePanel() {
        JPanel loadSavePanel = new JPanel(new GridLayout(1, 2));

        loadButton = new JButton();
        Icon loadIcon = UIManager.getIcon("FileView.directoryIcon");
        loadButton.setIcon(loadIcon);
        loadButton.addActionListener(this);
        saveButton = new JButton();
        Icon saveIcon = UIManager.getIcon("FileView.floppyDriveIcon");
        saveButton.setIcon(saveIcon);
        saveButton.addActionListener(this);
        loadSavePanel.add(loadButton);
        loadSavePanel.add(saveButton);
        loadSavePanel.setMaximumSize(new Dimension(50, 20));
        return loadSavePanel;
    }

    // EFFECTS: helps users select files to load
    public JFileChooser prepareFileChooser() {
        JFileChooser chooser = new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir"));
        chooser.setCurrentDirectory(workingDirectory);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Pantry saved files, *.json", "json");
        chooser.addChoosableFileFilter(restrict);
        return chooser;
    }

    // EFFECTS: updates list of ingredients everytime something is added
    public void updateList() {
        String category = (String) categoryCombo.getSelectedItem();
        if (category != null) {
            DefaultListModel<Ingredient> model = (DefaultListModel<Ingredient>) ingredientList.getModel();
            model.clear();
            List<Ingredient> ingredients = pantry.getIngredientsByCategory(FoodCategories.valueOf(category));
            model.addAll(ingredients);
        }
    }

    // EFFECTS: calls a function based off of what action the user has performed
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == categoryCombo) {
            actionCategoryButtons();
        }
        if (ae.getSource() == loadButton) {
            actionLoadButton();
        }
        if (ae.getSource() == saveButton) {
            actionSaveButton();
        }
        if (ae.getSource() == addButton) {
            addButton();
        }
        if (ae.getSource() == useButton) {
            useButton();
        }
    }

    // EFFECTS: calls functionality from UseIngredientDialogue so users can use their ingredients
    private void useButton() {
        UseIngredientDialogue useDialog = new UseIngredientDialogue(this, ingredientList.getSelectedValue());
        useDialog.showUseDialogue();
        updateIngredientSummary(ingredientList.getSelectedValue());
    }

    // EFFECTS: calls functionality from AddIngredientDialogue so users can add ingredients
    public void addButton() {
        AddIngredientDialogue addDialog = new AddIngredientDialogue(this, pantry, ingredientList.getSelectedValue(),
                (String) categoryCombo.getSelectedItem());
        addDialog.showAddDialogue();
        Ingredient i = ingredientList.getSelectedValue();
        updateList();
        if (i != null) {
            ingredientList.setSelectedValue(i, true);
        }
    }

    // EFFECTS: displays the categories for users to select from
    public void actionCategoryButtons() {
        updateList();
        String category = (String) categoryCombo.getSelectedItem();
        try {
            String imagePath = getImageFile(category);
            BufferedImage image = ImageIO.read(new File(imagePath));
            Image scaledImage = image.getScaledInstance((int) (image.getWidth() / 1.5),
                    (int) (image.getHeight() / 1.5), Image.SCALE_SMOOTH);
            picLabel.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.out.println("SOMETHING IS WRONG");
        }
    }

    // EFFECTS: get the photos used for each category
    public static String getImageFile(String category) {
        String imagePath = "./img/others-small.png";
        switch (category) {
            case "PROTEIN":
                imagePath = "./img/protein-small.png";
                break;
            case "DAIRY":
                imagePath = "./img/dairy-small.png";
                break;
            case "FRUIT_AND_VEG":
                imagePath = "./img/vegfruit-small.png";
                break;
            case "WHEAT":
                imagePath = "./img/wheat-small.png";
                break;
            default:
                break;
        }
        return imagePath;
    }

    // EFFECTS: allows users to save their progress to a file
    public void actionSaveButton() {
        JFileChooser chooser = prepareFileChooser();
        int r = chooser.showSaveDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            JSonWriter writer = new JSonWriter(chooser.getSelectedFile().getAbsolutePath());
            try {
                writer.open();
                writer.write(pantry);
            } catch (Exception e) {
                System.out.println("SOMETHING IS WRONG");
            } finally {
                writer.close();
            }
        }
    }

    // EFFECTS: allows users to load their progress to a file
    public void actionLoadButton() {
        JFileChooser chooser = prepareFileChooser();
        int r = chooser.showOpenDialog(this);
        if (r == JFileChooser.APPROVE_OPTION) {
            String pantryFilePath = chooser.getSelectedFile().getAbsolutePath();
            JSonReader jsonReader = new JSonReader(pantryFilePath);
            try {
                pantry = jsonReader.read();
                categoryCombo.setEnabled(true);
                addButton.setEnabled(true);
            } catch (IOException e) {
                System.out.println("SOMETHING IS WRONG");
            }
        }
    }

    // EFFECTS: updates amount and expiration labels for the ingredient
    public void updateIngredientSummary(Ingredient ingredient) {
        amountValueLabel.setText(String.valueOf(ingredient.getAmount()));
        expirationLabel.setText(ingredient.numOfExpiredFood(LocalDate.now()) != 0 ? "Yes" : "No");
    }

    // EFFECTS: updates the ingredient summary if adding a completely new ingredient
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == ingredientList) {
            Ingredient ingredient = ingredientList.getSelectedValue();
            if (ingredient != null) {
                updateIngredientSummary(ingredient);
                useButton.setEnabled(true);
            } else {
                amountValueLabel.setText("");
                expirationLabel.setText("");
                useButton.setEnabled(false);
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
        // do nothing
    }

    @Override
    public void windowClosing(WindowEvent e) {
        for (Event next : EventLog.getInstance()) {
            System.out.println(next.toString());
        }
        EventLog.getInstance().clear();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
        // do nothing
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // do nothing
    }

    @Override
    public void windowActivated(WindowEvent e) {
        // do nothing
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // do nothing
    }
}
