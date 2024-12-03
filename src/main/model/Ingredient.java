package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JSonConversion;

import java.time.LocalDate;
import java.util.*;

// Represents a list of every purchase of this food item since the start of the pantry. Tracks the name of the food,
// the category of food it falls under, and lastly the list of ingredients
public class Ingredient implements JSonConversion {
    private final String name;                     // name of the food
    private final List<Purchase> listOfPurchase; // list of purchases of the food that is still in the pantry
    private final FoodCategories category;         // food category the ingredients falls under
    private final boolean isLiquid;  // whether the food is a liquid or not
    private final boolean isIndividual; // whether the food is counted individually or not

    // REQUIRES: name has length > 0
    // EFFECTS: creates an ingredient with its name, category, listOfPurchase which holds all current inventory of
    // the food, and whether it is liquid or it is counted individually
    public Ingredient(String name, FoodCategories category, boolean isLiquid, boolean isIndividual) {
        this.name = name;
        this.category = category;
        listOfPurchase = new LinkedList<>();
        this.isLiquid = isLiquid;
        this.isIndividual = isIndividual;
    }

    //getters
    public String getName() {
        return name;
    }

    public FoodCategories getCategory() {
        return category;
    }

    public boolean getIsLiquid() {
        return isLiquid;
    }

    public List<Purchase> getListOfPurchase() {
        return listOfPurchase;
    }

    public boolean getIsIndividual() {
        return isIndividual;
    }

    // REQUIRES: Ingredient list length > 1
    // EFFECTS: returns the number of items you have of this item in your pantry
    public int getAmount() {
        int totFood = 0;
        for (Purchase p : listOfPurchase) {
            totFood += p.getAmount();
        }
        return totFood;
    }


    // REQUIRES: amountToRemove <= IngredientList.getAmount()
    // MODIFIES: this
    // EFFECTS: removes amountToRemove total ingredient from list, using the one closest to its expiration
    public void useIngredient(int amountToRemove) {
        int removed = amountToRemove;
        while (amountToRemove > 0) {
            Purchase pur = closestToExpiration();
            int purAmount = pur.getAmount();
            if (amountToRemove > purAmount) {
                pur.setAmount(0);
                listOfPurchase.remove(pur);
                amountToRemove -= purAmount;
            } else if (amountToRemove == purAmount) {
                pur.setAmount(0);
                listOfPurchase.remove(pur);
                amountToRemove = 0;
            } else {
                pur.removeAmount(amountToRemove);
                amountToRemove = 0;
            }
        }
        EventLog.getInstance().logEvent(new Event("Used " + removed + " " + name));
    }

    public Purchase closestToExpiration() {
        LocalDate comparison = listOfPurchase.get(0).getExpirationDate();
        Purchase aboutToExpire = listOfPurchase.get(0);
        for (Purchase p: listOfPurchase) {
            if (p.getExpirationDate().isBefore(comparison)) {
                comparison = p.getExpirationDate();
                aboutToExpire = p;
            }
        }
        return aboutToExpire;
    }

    // MODIFIES: this
    // EFFECTS: adds a log of purchase to the end of the list
    public void addPurchase(Purchase pur) {
        listOfPurchase.add(pur);
        EventLog.getInstance().logEvent(new Event("Added " + pur.getAmount() + " of " + name
                + " to the pantry"));
    }

    // REQUIRES: listOfIngredient.size() > 0
    // EFFECTS: returns the amount of expired food from all purchases in this
    public int numOfExpiredFood(LocalDate currDate) {
        int totExpired = 0;
        for (Purchase p : listOfPurchase) {
            if (p.isExpired(currDate)) {
                totExpired += p.getAmount();
            }
        }
        return totExpired;
    }

    // REQUIRES: listOfIngredient.size() > 0
    // EFFECTS: returns true if the list contains any expired food in it
    public boolean containsExpired(LocalDate currDate) {
        for (Purchase p : listOfPurchase) {
            if (p.isExpired(currDate)) {
                return true;
            }
        }
        return false;
    }

    // REQUIRES: listOfIngredient.size() > 0
    // MODIFIES: this
    // EFFECTS: removes all instances of expired purchases of the list
    public void removeExpiredFood(LocalDate currDate) {
        List<Purchase> toRemove = new LinkedList<>();
        for (Purchase p : listOfPurchase) {
            if (p.isExpired(currDate)) {
                toRemove.add(p);
            }
        }
        for (Purchase p : toRemove) {
            listOfPurchase.remove(p);
        }
    }


    // EFFECTS: converts the Ingredient object to a JSonObject
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("listOfPurchase", purchasesToJson());
        jsonObject.put("category", category);
        jsonObject.put("isLiquid", isLiquid);
        jsonObject.put("isIndividual", isIndividual);
        return jsonObject;
    }

    // EFFECTS: returns purchases inside the Ingredient as a JSONArray
    public JSONArray purchasesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Purchase p : listOfPurchase) {
            jsonArray.put(p.toJson());
        }
        return jsonArray;
    }

    // EFFECTS: instead of returning object, returns name of ingredient when toString is called
    public String toString() {
        return this.getName();
    }
}
