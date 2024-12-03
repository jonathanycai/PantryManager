package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JSonConversion;
import persistence.JSonWriter;

import java.time.LocalDate;
import java.util.*;

// This is the main piece of the project. The pantry method stores all the food and can extract information of
// each IngredientList which is stored in it. Moreover, it can also remove and add IngredientLists and remove
// any expired food from the purchases
public class Pantry implements JSonConversion {
    private final List<Ingredient> listOfAllIngredients; // all the food currently stored in the pantry

    // EFFECTS: creates a pantry item and in doing so, creates a listOfAllIngredients
    public Pantry() {
        listOfAllIngredients = new LinkedList<>();
    }

    //getters
    public List<Ingredient> getListOfAllIngredients() {
        return listOfAllIngredients;
    }

    // REQUIRES: listOfAllFood.size() > 0
    // EFFECTS: returns all the IngredientLists of the inputted FoodCategory
    public List<Ingredient> getIngredientsByCategory(FoodCategories category) {
        List<Ingredient> listInCategory = new LinkedList<>();
        for (Ingredient i : listOfAllIngredients) {
            if (i.getCategory() == category) {
                listInCategory.add(i);
            }
        }
        return listInCategory;
    }

    // REQUIRES: listOfAllFood.size() > 0
    // EFFECTS: returns the IngredientList called on by the method
    public Ingredient getFoodByName(String name) {
        for (Ingredient i : listOfAllIngredients) {
            if (name.equalsIgnoreCase(i.getName())) {
                return i;
            }
        }
        return null;
    }

    // REQUIRES: listOfAllFood.size() > 0
    // MODIFIES: this
    // EFFECTS: adds an IngredientList to the pantry
    public void addIngredientList(Ingredient ingList) {
        listOfAllIngredients.add(ingList);
        EventLog.getInstance().logEvent(new Event("Added " + ingList.toString() + " to the pantry"));
    }

    // REQUIRES: listOfAllFood.size() > 0 && listOfAllIngredients.contains(ingList)
    // MODIFIES: this
    // EFFECTS: remove the ingredient list from the pantry
    public void removeIngredientList(Ingredient ingList) {
        listOfAllIngredients.remove(ingList);
    }

    // REQUIRES: listOfAllFood.size() > 0
    // EFFECTS: returns the foods of expired purchases in the pantry
    public List<String> totExpiredIngredients(LocalDate currDate) {
        List<String> expiredFood = new LinkedList<>();
        for (Ingredient ingList : listOfAllIngredients) {
            if (ingList.containsExpired(currDate)) {
                expiredFood.add(ingList.numOfExpiredFood(currDate) + " " + ingList.getName());
            }
        }
        return expiredFood;
    }

    // REQUIRES: listOfAllFood.size() > 0
    // EFFECTS: removes all the expired purchases in the pantry
    public void removeAllExpiredFood(LocalDate currDate) {
        for (Ingredient ingList : listOfAllIngredients) {
            ingList.removeExpiredFood(currDate);
        }
    }

    // EFFECTS: returns Pantry as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list of all ingredients", convertListOfAllIngredients());
        return jsonObject;
    }


    // EFFECTS: returns the listOfAllIngredients inside the Pantry as a JSONArray
    public JSONArray convertListOfAllIngredients() {
        JSONArray jsonArray = new JSONArray();
        for (Ingredient i : listOfAllIngredients) {
            jsonArray.put(i.toJson());
        }
        return jsonArray;
    }
}
