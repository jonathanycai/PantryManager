package ui;

import model.*;
import persistence.JSonReader;
import persistence.JSonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

//PantryApp application
public class PantryApp {
    private static final String destination = "./data/pantry.json";
    private JSonWriter jsonWriter;
    private JSonReader jsonReader;
    private Scanner input;
    private Pantry pantry;


    // EFFECTS: initializes the ui
    public PantryApp() {
        runPantry();
    }

    // EFFECTS: initializes the PantryApp
    public void runPantry() {
        boolean keepGoing = true;
        String command;
        input = new Scanner(System.in);
        pantry = new Pantry();
        jsonWriter = new JSonWriter(destination);
        jsonReader = new JSonReader(destination);
        loadPantry();
        System.out.println("WELCOME TO YOUR PANTRYAPP!");
        while (keepGoing) {
            displayOptions();
            command = input.next();
            if (command.equals("9")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
        savePantry();
        System.out.println("Thank you for using the PantryApp!");
    }

    // EFFECTS: loads in the most recent state of the pantry
    public void loadPantry() {
        try {
            pantry = jsonReader.read();
            System.out.println("Loaded pantry...");
        } catch (IOException e) {
            // doesn't need to do anything, first time using
        }
    }

    // EFFECTS: saves the current state of the pantry
    public void savePantry() {
        try {
            jsonWriter.open();
            jsonWriter.write(pantry);
            jsonWriter.close();
            System.out.println("Saved pantry!");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to save pantry");
        }
    }

    // EFFECTS: prints out the features users can use
    public void displayOptions() {
        System.out.println("\nPlease select from the following options:");
        System.out.println("[1] Create new list for an item pantry");
        System.out.println("[2] Add item to existing list for pantry");
        System.out.println("[3] View Pantry");
        System.out.println("[4] Search for an ingredient in the pantry");
        System.out.println("[5] Use an ingredient in the pantry");
        System.out.println("[6] Scan for expired food");
        System.out.println("[7] Load a previous pantry");
        System.out.println("[8] Save your pantry");
        System.out.println("[9] Exit pantry\n");
        System.out.print("Please type in the corresponding number to your selection: ");
    }

    // EFFECTS: Calls the corresponding function for the command the user inputted
    public void processCommand(String command) {
        if (command.equals("1")) {
            createNewItem();
        } else if (command.equals("2")) {
            addToExistingItem();
        } else if (command.equals("3")) {
            viewPantry();
        } else if (command.equals("4")) {
            searchForItem();
        } else if (command.equals("5")) {
            useFood();
        } else if (command.equals("6")) {
            scanForExpiredFood();
        } else if (command.equals("7")) {
            loadPantry();
        } else if (command.equals("8")) {
            savePantry();
        } else {
            System.out.println("That is not a valid input!");
        }
    }

    // MODIFIES: pantry
    // EFFECTS: adds an ingredientList to the pantry
    public void createNewItem() {
        System.out.println("\nPlease answer the following questions:");
        Ingredient newIngredient = handleQuestionsIngredient();
        if (newIngredient == null) {
            return;
        }
        pantry.addIngredientList(newIngredient);
    }

    // EFFECTS: creates and returns an ingredient based off user input
    public Ingredient handleQuestionsIngredient() {
        FoodCategories category;
        boolean isIndividual;
        String name = handleName();
        if (pantry.getFoodByName(name) != null) {
            System.out.println("This ingredient already exists in your pantry!");
            return null;
        } else if (name.equals("quit")) {
            return null;
        }
        printFoodCategories();
        System.out.print("What food category would you like to place " + name + " under?: ");
        String command = input.next();
        if (checkEnumeration(command)) {
            System.out.println("This is not a valid category");
            return null;
        }
        category = FoodCategories.valueOf(command.toUpperCase());
        boolean isLiquid = handleIsLiquid();
        isIndividual = checkIndividual(isLiquid);
        Ingredient ingredient = new Ingredient(name, category, isLiquid, isIndividual);
        return ingredient;
    }

    // EFFECTS: creates the name for the ingredient created in handleQuestionsIngredient
    public String handleName() {
        System.out.print("What would you like to name this ingredient (enter quit to exit): ");
        return input.next();
    }

    // EFFECTS: returns isIndividual as true if isLiquid is true, else calls handleIndividual to let user decide
    public boolean checkIndividual(boolean isLiquid) {
        boolean isIndividual;
        if (isLiquid) {
            isIndividual = true;
        } else {
            isIndividual = handleIndividual();
        }
        return isIndividual;
    }

    // EFFECTS: creates the boolean isLiquid for the ingredient created in handleQuestionsIngredient
    public boolean handleIsLiquid() {
        System.out.print("\nIs this ingredient liquid? (input true or false): ");
        return input.nextBoolean();
    }

    // EFFECTS: creates the boolean isIndividual for the ingredient created in handleQuestionsIngredient
    public boolean handleIndividual() {
        System.out.print("\nIs this item measured individually? (input true or false if measured in grams): ");
        return input.nextBoolean();
    }

    // EFFECTS: prints the food categories available for users to choose from
    public void printFoodCategories() {
        System.out.println("PROTEIN | DAIRY | FRUIT_AND_VEG | WHEAT | OTHER\n");
    }

    // MODIFIES: pantry
    // EFFECTS: allows users to add more items to pre-existing items in the pantry
    public void addToExistingItem() {
        String command;
        if (handleEmpty()) {
            System.out.println("You have nothing in your pantry, please select another option\n");
            return;
        }
        Ingredient ingredientAddTo = searchForItemSearchBar();
        if (ingredientAddTo == null) {
            System.out.println("You currently do not have this item in your pantry");
            return;
        }
        handleAdding(ingredientAddTo);
        System.out.print("Would you like to add another item? (yes/no): ");
        command = input.next();
        if (command.equalsIgnoreCase("yes")) {
            addToExistingItem();
        }
    }

    // MODIFIES: ingredientAddTo
    // EFFECTS: creates and adds a Purchase to ingredientAddTo
    public void handleAdding(Ingredient ingredientAddTo) {
        Purchase purchaseToAdd;
        int amount = manageAmount();
        if (amount == -1) {
            return;
        }
        LocalDate expiryDate = manageExpiration();
        purchaseToAdd = new Purchase(amount, expiryDate);
        ingredientAddTo.addPurchase(purchaseToAdd);
    }

    // EFFECTS: returns the expiration date of the purchase the user wants to add
    public LocalDate manageExpiration() {
        LocalDate expiryDate = LocalDate.of(1, 1, 1);
        System.out.print("When is the expiry date? (dd/mm/yyyy): ");
        String expiryDateString = input.next();
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            expiryDate = LocalDate.parse(expiryDateString, formatter);
        } catch (DateTimeParseException d) {
            System.out.println("That is not a valid date, please try again");
            manageExpiration();
        }
        return expiryDate;
    }

    // EFFECTS: returns the amount of items the user wants to add to this purchase
    public int manageAmount() {
        int amount = 0;
        System.out.print("How many items would you like to add: ");
        try {
            amount = input.nextInt();
        } catch (InputMismatchException i) {
            System.out.println("That is not a valid input");
            input.next();
            return - 1;
        }
        if (amount <= 0) {
            System.out.print("That is not a valid input");
            return -1;
        }
        return amount;
    }

    // EFFECTS: allows users to view their pantry according to the different food categories
    public void viewPantry() {
        FoodCategories category;
        String command;
        if (handleEmpty()) {
            System.out.println("You have nothing in your pantry, please select another option\n");
            return;
        }
        printFoodCategories();
        System.out.print("Which food category would you like to view (input back to return to main menu): ");
        command = input.next();
        if (command.equalsIgnoreCase("back")) {
            return;
        } else if (checkEnumeration(command)) {
            System.out.println("This is not a valid category");
            return;
        }
        category = FoodCategories.valueOf(command.toUpperCase());
        viewIngredients(category);
    }

    // EFFECTS: returns if the string matches the string version of the FoodCategories
    public boolean checkEnumeration(String category) {
        return !(category.equalsIgnoreCase("FRUIT_AND_VEG")
                || category.equalsIgnoreCase("PROTEIN")
                || category.equalsIgnoreCase("DAIRY")
                || category.equalsIgnoreCase("WHEAT")
                || category.equalsIgnoreCase("OTHER"));
    }

    // EFFECTS: returns true if there is nothing inside the pantry
    public boolean handleEmpty() {
        return pantry.getListOfAllIngredients().size() == 0;
    }

    // EFFECTS: displays all the ingredients currently in this FoodCategory
    public void viewIngredients(FoodCategories category) {
        List<Ingredient> ingredientsToView;
        ingredientsToView = pantry.getIngredientsByCategory(category);
        String command;

        System.out.print("Would you like to continue or go back (input continue or back): ");
        command = input.next();
        if (command.equals("back")) {
            return;
        } else if (ingredientsToView.size() == 0) {
            System.out.println("You currently have no foods in this food group");
            return;
        }
        System.out.println("Here is a list of all ingredients currently in this food group: \n");
        for (Ingredient i : ingredientsToView) {
            System.out.println("- " + i.getName());
        }
        System.out.print("\nWhich food item would you like to print out?: ");
        command = input.next();
        viewPurchases(ingredientsToView, command);
    }

    // EFFECTS: displays all the Purchases of a specified ingredient if it exists or if there is more than one instance
    // currently in the pantry
    public void viewPurchases(List<Ingredient> ingredientsToView, String ingredientName) {
        Ingredient ingredient = null;
        for (Ingredient i : ingredientsToView) {
            if (i.getName().equalsIgnoreCase(ingredientName)) {
                ingredient = i;
            }
        }
        if (ingredient == null) {
            System.out.println("Your item was not found!");
            viewPantry();
        } else if (ingredient.getListOfPurchase().size() == 0) {
            System.out.println("You currently do not have this ingredient");
        } else {
            printPurchaseList(ingredient);
        }
    }

    // EFFECTS: prints the list of purchases made for the inputted ingredient
    public void printPurchaseList(Ingredient ingredient) {
        System.out.print("\nYou currently have " + ingredient.getAmount() + " ");
        if (ingredient.getIsLiquid()) {
            System.out.print("mL of " + ingredient.getName());
        } else if (!ingredient.getIsIndividual()) {
            System.out.print("g of " + ingredient.getName());
        } else {
            System.out.print(ingredient.getName());
        }
        System.out.println();
        int count = 1;
        String measurement = "";
        if (ingredient.getIsLiquid()) {
            measurement = "mL";
        } else if (!ingredient.getIsIndividual()) {
            measurement = "g";
        }
        for (Purchase p : ingredient.getListOfPurchase()) {
            System.out.println("Purchase [" + count + "]:\n"
                    + "Amount: " + p.getAmount() + measurement + "\nExpiration Date: "
                    + p.getExpirationDate().toString() + "\n");
            count++;
        }
    }

    // EFFECTS: returns Ingredient according to name inputted, returns null if it doesn't exist
    public Ingredient searchForItemSearchBar() {
        String search;
        System.out.print("\nWhich item would you like to look for? ");
        search = input.next();
        for (Ingredient i : pantry.getListOfAllIngredients()) {
            if (i.getName().equalsIgnoreCase(search)) {
                return i;
            }
        }
        return null;
    }

    // MODIFIES: pantry
    // EFFECTS: uses food for a specified ingredient according to nearest to the expiration date
    public void useFood() {
        if (handleEmpty()) {
            System.out.println("You have nothing in your pantry, please select another option\n");
            return;
        }
        Ingredient ingredientToUse = searchForItemSearchBar();
        if (ingredientToUse == null) {
            System.out.println("You currently do not have this item in your pantry");
            return;
        }
        System.out.println("How much would you like to use (enter integer value): ");
        int amountToUse = input.nextInt();
        while (amountToUse > ingredientToUse.getAmount()) {
            System.out.println("That is more than you currently have!");
            System.out.print("Please try again or enter -1 to quit");
            amountToUse = input.nextInt();
        }
        if (amountToUse == -1) {
            return;
        } else {
            ingredientToUse.useIngredient(amountToUse);
        }
    }

    // EFFECTS: allows users to search for a specific Ingredient in their pantry and print their total purchases
    public void searchForItem() {
        if (handleEmpty()) {
            System.out.println("You have nothing in your pantry, please select another option\n");
            return;
        }
        Ingredient ingredientToPrint = searchForItemSearchBar();
        if (ingredientToPrint == null) {
            System.out.println("You currently do not have this item in your pantry");
            return;
        } else {
            printPurchaseList(ingredientToPrint);
        }
    }

    // MODIFIES: pantry
    // EFFECTS: searches the pantry for expired food and if it is found, then removes it from list if user wants to
    public void scanForExpiredFood() {
        LocalDate today = LocalDate.now();
        List<String> listOfExpiredIngredients = pantry.totExpiredIngredients(today);
        if (listOfExpiredIngredients.size() == 0) {
            System.out.println("You have no expired ingredients\n");
        } else {
            for (String s : listOfExpiredIngredients) {
                System.out.println(s);
            }
            System.out.print("Would you like to remove all expired ingredients (yes/no): ");
            String remove = input.next();
            if (remove.equalsIgnoreCase("yes")) {
                pantry.removeAllExpiredFood(today);
            }
        }
    }
}
