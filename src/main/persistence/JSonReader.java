package persistence;

import model.FoodCategories;
import model.Ingredient;
import model.Pantry;
import model.Purchase;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.stream.Stream;

public class JSonReader {

    private String destination;

    // Represents a reader that reads a Pantry stored in a JSON file
    public JSonReader(String destination) {
        this.destination = destination;
    }

    // EFFECTS: constructs a reader that reads a Pantry stored in JSON data
    public Pantry read() throws IOException {
        String data = readFile(destination);
        JSONObject jsonObject = new JSONObject(data);
        return parsePantry(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    public String readFile(String destination) throws IOException {
        StringBuilder fileInfo = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(destination), StandardCharsets.UTF_8)) {
            stream.forEach(fileInfo::append);
        }
        return fileInfo.toString();
    }

    // EFFECTS: parses Pantry from JSON file and returns it
    public Pantry parsePantry(JSONObject jsonObject) {
        Pantry pantry = new Pantry();
        addIngredients(pantry, jsonObject);
        return pantry;
    }

    // EFFECTS: parses Ingredients from JSON file and adds it to Pantry
    public void addIngredients(Pantry pantry, JSONObject jsonObject) {
        JSONArray listOfAllIngredients = jsonObject.getJSONArray("list of all ingredients");
        for (Object obj : listOfAllIngredients) {
            JSONObject ingredient = (JSONObject) obj;
            createIngredient(pantry, ingredient);
        }
    }

    // EFFECTS: adds the individual Ingredients and adds it to the list inside the Pantry Object
    public void createIngredient(Pantry pantry, JSONObject ingredientObj) {
        String name = ingredientObj.getString("name");
        FoodCategories category = FoodCategories.valueOf(ingredientObj.getString("category"));
        Boolean isLiquid = ingredientObj.getBoolean("isLiquid");
        Boolean isIndividual = ingredientObj.getBoolean("isIndividual");
        Ingredient ingredient = new Ingredient(name, category, isLiquid, isIndividual);
        addPurchases(ingredient, ingredientObj);
        pantry.addIngredientList(ingredient);
    }

    // EFFECTS: parses Ingredients from JSON file and adds it to each Ingredient inside the pantry
    public void addPurchases(Ingredient ingredient, JSONObject ingredientObj) {
        JSONArray listOfPurchase = ingredientObj.getJSONArray("listOfPurchase");
        for (Object obj : listOfPurchase) {
            JSONObject purchase = (JSONObject) obj;
            createPurchase(ingredient, purchase);
        }
    }

    // EFFECTS: adds Purchases to the list inside Ingredient inside the Pantry
    public void createPurchase(Ingredient ingredient, JSONObject purchaseObj) {
        int amount = purchaseObj.getInt("amount");
        LocalDate expiryDate = LocalDate.parse(purchaseObj.getString("expiration date"));
        Purchase purchase = new Purchase(amount, expiryDate);
        ingredient.addPurchase(purchase);
    }

}
