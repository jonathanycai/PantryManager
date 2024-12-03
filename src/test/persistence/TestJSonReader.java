package persistence;

import model.FoodCategories;
import model.Ingredient;
import model.Pantry;
import model.Purchase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class TestJSonReader extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JSonReader reader = new JSonReader("./data/randomFile.json");
        try {
            Pantry p = reader.read();
            fail("IOException not thrown");
        } catch (IOException e) {
            // this is supposed to happen
        }
    }

    @Test
    void testReaderEmptyPantry() {
        JSonReader reader = new JSonReader("./data/testReaderEmptyPantry.json");
        try {
            Pantry p = reader.read();
            assertEquals(0, p.getListOfAllIngredients().size());
        } catch (IOException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    void testReaderPantryOnlyIngredients() {
        JSonReader reader = new JSonReader("./data/testReaderPantryOnlyIngredient.json");
        try {
            Pantry p = reader.read();
            List<Ingredient> ingredients = p.getListOfAllIngredients();
            List<Purchase> emptyList = new ArrayList<>();
            assertEquals(2, ingredients.size());
            checkIngredient("Sausage", FoodCategories.PROTEIN, emptyList,
                    false, true, ingredients.get(0));
            checkIngredient("Potatoes", FoodCategories.FRUIT_AND_VEG, emptyList,
                    false, true, ingredients.get(1));
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }

    @Test
    void testReaderGeneralPantry() {
        JSonReader reader = new JSonReader("./data/testReaderGeneralPantry.json");
        try {
            Pantry p = reader.read();
            List<Ingredient> ingredients = p.getListOfAllIngredients();
            List<Purchase> potatoPurchase = new ArrayList<>();
            assertEquals(2, ingredients.size());
            Purchase pot = new Purchase(4, LocalDate.of(0001, 1, 1));
            potatoPurchase.add(pot);
            checkIngredient("Potato", FoodCategories.FRUIT_AND_VEG,
                    potatoPurchase, false, true, ingredients.get(0));
            List<Purchase> milkPurchase = new ArrayList<>();
            Purchase milk = new Purchase(250, LocalDate.of(2026, 6, 2));
            milkPurchase.add(milk);
            checkIngredient("Milk", FoodCategories.DAIRY,
                    milkPurchase, true, true, ingredients.get(1));
        } catch (IOException e) {
            fail("UnExpected IOException thrown");
        }
    }

}
