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

public class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Pantry pantry = new Pantry();
            JSonWriter writer = new JSonWriter("./data/my\0989898: wtf.json");
            writer.open();
            fail("IOException not thrown");
        } catch (IOException e) {
            // supposed to happen
        }
    }

    @Test
    void testWriterEmptyPantry() {
        try {
            Pantry p = new Pantry();
            JSonWriter writer = new JSonWriter(("./data/testWriterEmptyPantry.json"));
            writer.open();
            writer.write(p);
            writer.close();

            JSonReader reader = new JSonReader("./data/testWriterEmptyPantry.json");
            p = reader.read();
            assertEquals(0, p.getListOfAllIngredients().size());
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }

    @Test
    void testWriterOnlyIngredients() {
        try {
            Pantry p = new Pantry();
            p.addIngredientList(new Ingredient("Sausage", FoodCategories.PROTEIN, false, true));
            p.addIngredientList(new Ingredient("Potatoes", FoodCategories.FRUIT_AND_VEG,
                    false, true));
            JSonWriter writer = new JSonWriter(("./data/testWriterOnlyIngredients.json"));
            writer.open();
            writer.write(p);
            writer.close();

            JSonReader reader = new JSonReader("./data/testWriterOnlyIngredients.json");
            p = reader.read();
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
    void testWriterGeneralPantry() {
        try {
            Pantry p = new Pantry();
            p.addIngredientList(new Ingredient("Potato", FoodCategories.FRUIT_AND_VEG,
                    false, true));
            p.addIngredientList(new Ingredient("Milk", FoodCategories.DAIRY,
                    true, true));
            p.getFoodByName("Potato").addPurchase(new Purchase(4,
                    LocalDate.of(2031, 6, 7)));
            p.getFoodByName("Milk").addPurchase(new Purchase(250,
                    LocalDate.of(2026, 6, 2)));
            JSonWriter writer = new JSonWriter(("./data/testWriterGeneralPantry.json"));
            writer.open();
            writer.write(p);
            writer.close();

            JSonReader reader = new JSonReader("./data/testWriterGeneralPantry.json");
            p = reader.read();
            List<Ingredient> ingredients = p.getListOfAllIngredients();
            List<Purchase> potatoPurchase = new ArrayList<>();
            assertEquals(2, ingredients.size());
            Purchase pot = new Purchase(4, LocalDate.of(2031, 6, 7));
            potatoPurchase.add(pot);
            checkIngredient("Potato", FoodCategories.FRUIT_AND_VEG,
                    potatoPurchase, false, true, ingredients.get(0));
            List<Purchase> milkPurchase = new ArrayList<>();
            Purchase milk = new Purchase(250, LocalDate.of(2026, 6, 2));
            milkPurchase.add(milk);
            checkIngredient("Milk", FoodCategories.DAIRY,
                    milkPurchase, true, true, ingredients.get(1));
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }
}
