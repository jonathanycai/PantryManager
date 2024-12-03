package persistence;

import model.FoodCategories;
import model.Ingredient;
import model.Purchase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonTest {
    protected void checkIngredient(String name, FoodCategories category,
                                   List<Purchase> lop, Boolean isLiquid, Boolean isIndividual, Ingredient ingredient) {
        assertEquals(name, ingredient.getName());
        assertEquals(category, ingredient.getCategory());
        assertEquals(lop.size(), ingredient.getListOfPurchase().size());
        for (int i = 0; i < lop.size(); i++) {
            checkPurchase(lop.get(i), ingredient.getListOfPurchase().get(i));
        }
        assertEquals(isLiquid, ingredient.getIsLiquid());
        assertEquals(isIndividual, ingredient.getIsIndividual());
    }

    protected void checkPurchase(Purchase p1, Purchase p2) {
        assertEquals(p1.getAmount(), p2.getAmount());
        assertEquals(p1.getExpirationDate(), p2.getExpirationDate());
    }
}
