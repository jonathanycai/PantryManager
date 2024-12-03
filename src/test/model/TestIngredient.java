package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static model.FoodCategories.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestIngredient {
    Ingredient testApple;
    Ingredient testCoke;
    Purchase testApplePurchase1;
    Purchase testApplePurchase2;
    Purchase testApplePurchase3;

    @BeforeEach
    void setUpTestIngredient() {
        testApple = new Ingredient("Apple", FRUIT_AND_VEG, false, true);
        testCoke = new Ingredient("Coke", FoodCategories.OTHER, true, false);
        testApplePurchase1 = new Purchase(4, LocalDate.of(2024, 2, 28));
        testApplePurchase2 = new Purchase(6, LocalDate.of(2024, 3, 4));
        testApplePurchase3 = new Purchase(1, LocalDate.of(2024, 2, 28));

    }

    @Test
    void testConstructor() {
        assertEquals("Apple", testApple.getName());
        assertEquals(FRUIT_AND_VEG, testApple.getCategory());
        assertEquals(0, testApple.getListOfPurchase().size());
        assertFalse(testApple.getIsLiquid());
        assertTrue(testApple.getIsIndividual());
    }

    @Test
    void testGetName() {
        assertEquals("Apple", testApple.getName());
    }

    @Test
    void testGetCategory() {
        assertEquals(FRUIT_AND_VEG, testApple.getCategory());
    }

    @Test
    void testGetListOfPurchase() {
        assertEquals(0, testApple.getListOfPurchase().size());
    }

    @Test
    void testIsLiquid() {
        assertFalse(testApple.getIsLiquid());
    }

    @Test
    void testIsIndividual() {
        assertTrue(testApple.getIsIndividual());
    }

    @Test
    void getAmountListSizeOne() {
        testApple.addPurchase(testApplePurchase1);
        assertEquals(4, testApple.getAmount());
    }

    @Test
    void getAmountListSizeLarger() {
        testApple.addPurchase(testApplePurchase1);
        testApple.addPurchase(testApplePurchase2);
        testApple.addPurchase(testApplePurchase3);
        assertEquals(11, testApple.getAmount());
    }

    @Test
    void useIngredientSizeOne() {
        testApple.addPurchase(testApplePurchase1);
        testApple.useIngredient(3);
        assertEquals(1, testApplePurchase1.getAmount());
        testApple.useIngredient(1);
        assertEquals(0, testApplePurchase1.getAmount());
        assertEquals(0, testApple.getListOfPurchase().size());
    }

    @Test
    void useIngredientSizeLarger() {
        testApple.addPurchase(testApplePurchase1);
        testApple.addPurchase(testApplePurchase2);
        testApple.addPurchase(testApplePurchase3);
        testApple.useIngredient(6);
        assertEquals(1, testApple.getListOfPurchase().size());
        assertEquals(5, testApplePurchase2.getAmount());
    }

    @Test
    void testClosestToExpiration() {
        testApple.addPurchase(testApplePurchase1);
        assertEquals(testApplePurchase1, testApple.closestToExpiration());
    }

    @Test
    void testClosestToExpirationSame() {
        testApple.addPurchase(testApplePurchase1);
        testApple.addPurchase(testApplePurchase3);
        assertEquals(testApplePurchase1, testApple.closestToExpiration());
    }

    @Test
    void testClosestToExpirationMultiple() {
        testApple.addPurchase(testApplePurchase2);
        testApple.addPurchase(testApplePurchase1);
        testApple.addPurchase(testApplePurchase3);
        assertEquals(testApplePurchase1, testApple.closestToExpiration());
    }

    @Test
    void testAddPurchaseOnce() {
        testApple.addPurchase(testApplePurchase3);
        assertEquals(1, testApple.getListOfPurchase().size());
    }

    @Test
    void testAddPurchaseMultipleTimes() {
        testApple.addPurchase(testApplePurchase2);
        testApple.addPurchase(testApplePurchase3);
        assertEquals(2, testApple.getListOfPurchase().size());
    }

    @Test
    void testNumOfExpiredFoodNone() {
        testApple.addPurchase(testApplePurchase2);
        assertEquals(0, testApple.numOfExpiredFood(LocalDate.of(2024, 3, 1)));
    }

    @Test
    void testNumOfExpiredFoodSizeOne() {
        testApple.addPurchase(testApplePurchase2);
        assertEquals(6, testApple.numOfExpiredFood(LocalDate.of(2024, 3, 8)));
    }

    @Test
    void testNumOfExpiredFoodSizeMultiple() {
        testApple.addPurchase(testApplePurchase1);
        testApple.addPurchase(testApplePurchase2);
        testApple.addPurchase(testApplePurchase3);
        assertEquals(5, testApple.numOfExpiredFood(LocalDate.of(2024, 3, 1)));
    }

    @Test
    void testContainsExpiredNone() {
        testApple.addPurchase(testApplePurchase1);
        assertFalse(testApple.containsExpired(LocalDate.of(2024, 2, 1)));
    }

    @Test
    void testContainsExpiredSizeOne() {
        testApple.addPurchase(testApplePurchase1);
        assertTrue(testApple.containsExpired(LocalDate.of(2024, 3, 1)));
    }

    @Test
    void testContainsExpiredMultipleTrue() {
        testApple.addPurchase(testApplePurchase1);
        testApple.addPurchase(testApplePurchase2);
        testApple.addPurchase(testApplePurchase3);
        assertTrue(testApple.containsExpired(LocalDate.of(2024, 3, 1)));
    }

    @Test
    void testContainsExpiredMultipleFalse() {
        testApple.addPurchase(testApplePurchase1);
        testApple.addPurchase(testApplePurchase2);
        testApple.addPurchase(testApplePurchase3);
        assertFalse(testApple.containsExpired(LocalDate.of(2024, 2, 1)));
    }

    @Test
    void removeExpiredFoodNone() {
        testApple.addPurchase(testApplePurchase1);
        testApple.removeExpiredFood(LocalDate.of(2024, 2, 1));
        assertEquals(1, testApple.getListOfPurchase().size());
    }

    @Test
    void removeExpiredFoodOne() {
        testApple.addPurchase(testApplePurchase1);
        testApple.removeExpiredFood(LocalDate.of(2024, 3, 1));
        assertEquals(0, testApple.getListOfPurchase().size());
    }

    @Test
    void removeExpiredFoodMultiple() {
        testApple.addPurchase(testApplePurchase1);
        testApple.addPurchase(testApplePurchase2);
        testApple.addPurchase(testApplePurchase3);
        testApple.removeExpiredFood(LocalDate.of(2024, 3, 1));
        assertEquals(1, testApple.getListOfPurchase().size());
    }

    @Test
    void removeExpiredFoodLargeListRemoveNone() {
        testApple.addPurchase(testApplePurchase1);
        testApple.addPurchase(testApplePurchase2);
        testApple.addPurchase(testApplePurchase3);
        testApple.removeExpiredFood(LocalDate.of(2024, 2, 1));
        assertEquals(3, testApple.getListOfPurchase().size());
    }

    @Test
    void testToString() {
        assertEquals("Apple", testApple.toString());
    }


}
