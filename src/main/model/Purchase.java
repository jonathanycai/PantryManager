package model;

import org.json.JSONObject;
import persistence.JSonConversion;

import java.time.LocalDate;

// represents each instance of a purchase. specifies the amount of food still remaining of this purchase and its
// expiration date.
public class Purchase implements JSonConversion {

    private int amount; // how much is still left
    private final LocalDate expirationDate; // expiration date of the food

    public Purchase(int amount, LocalDate expirationDate) {
        this.amount = amount;
        this.expirationDate = expirationDate;
        // expiration date will be initialized here
    }

    //getters
    public int getAmount() {
        return amount;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    // REQUIRES: amountToRemove <= this.amount
    // MODIFIES: this
    // EFFECTS: removes a amountToRemove amount of food from this.amount
    public void removeAmount(int amountToRemove) {
        amount -= amountToRemove;
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: sets the amount from this purchase to the input
    public void setAmount(int amount) {
        this.amount = amount;
    }

    // EFFECTS: checks if the food from this purchase is expired
    public boolean isExpired(LocalDate currDate) {
        return (currDate.isAfter(expirationDate) || currDate.isEqual(expirationDate));
    }

    // EFFECTS: returns purchases in the Pantry as a JSON object
    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", amount);
        jsonObject.put("expiration date", expirationDate);
        return jsonObject;
    }
}
