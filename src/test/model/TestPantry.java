package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static model.FoodCategories.DAIRY;
import static model.FoodCategories.FRUIT_AND_VEG;
import static org.junit.jupiter.api.Assertions.*;

public class TestPantry {
    Pantry testPantry = new Pantry();
    Ingredient testApple;
    Ingredient testOrange;
    Ingredient testCoke;

    @BeforeEach
    void setUpTestPantry() {
        testPantry = new Pantry();
        testApple = new Ingredient("Apple", FRUIT_AND_VEG, false, true);
        testOrange = new Ingredient("Orange", FRUIT_AND_VEG, false, true);
        testCoke = new Ingredient("Coke", FoodCategories.OTHER, true, false);
    }

    @Test
    void testConstructor() {
        assertEquals(0, testPantry.getListOfAllIngredients().size());
    }

    @Test
    void testGetListOfAllIngredient() {
        testPantry.addIngredientList(testApple);
        assertEquals(1, testPantry.getListOfAllIngredients().size());
    }

    @Test
    void testGetIngredientByCategoryOneEmpty() {
        testPantry.addIngredientList(testApple);
        assertEquals(0, testPantry.getIngredientsByCategory(DAIRY).size());
    }

    @Test
    void testGetIngredientByCategoryOne() {
        testPantry.addIngredientList(testApple);
        assertEquals(1, testPantry.getIngredientsByCategory(FRUIT_AND_VEG).size());
    }

    @Test
    void testIngredientByCategoryMultipleReturnOne() {
        testPantry.addIngredientList(testApple);
        testPantry.addIngredientList(testCoke);
        assertEquals(1, testPantry.getIngredientsByCategory(FRUIT_AND_VEG).size());
    }

    @Test
    void testIngredientByCategoryMultipleReturnNone() {
        testPantry.addIngredientList(testApple);
        testPantry.addIngredientList(testCoke);
        assertEquals(0, testPantry.getIngredientsByCategory(DAIRY).size());
    }

    @Test
    void testIngredientByCategoryMultipleReturnAll() {
        testPantry.addIngredientList(testApple);
        testPantry.addIngredientList(testOrange);
        assertEquals(2, testPantry.getIngredientsByCategory(FRUIT_AND_VEG).size());
    }

    @Test
    void testGetFoodByNameEmpty() {
        testPantry.addIngredientList(testApple);
        assertNull(testPantry.getFoodByName("Banana"));
    }

    @Test
    void testGetFoodByNameOne() {
        testPantry.addIngredientList(testApple);
        assertEquals(testApple, testPantry.getFoodByName("Apple"));
    }

    @Test
    void testGetFoodByNameMultiple() {
        testPantry.addIngredientList(testApple);
        testPantry.addIngredientList(testOrange);
        testPantry.addIngredientList(testCoke);
        assertEquals(testApple, testPantry.getFoodByName("Apple"));
    }

    @Test
    void testGetFoodByNameMultipleReturnNone() {
        testPantry.addIngredientList(testApple);
        testPantry.addIngredientList(testOrange);
        testPantry.addIngredientList(testCoke);
        assertNull(testPantry.getFoodByName("Banana"));
    }

    @Test
    void testAddIngredientOnce() {
        testPantry.addIngredientList(testCoke);
        assertEquals(1, testPantry.getListOfAllIngredients().size());
    }

    @Test
    void testAddIngredientTwice() {
        testPantry.addIngredientList(testCoke);
        testPantry.addIngredientList(testApple);
        assertEquals(2, testPantry.getListOfAllIngredients().size());
    }

    @Test
    void testRemoveIngredientOnce() {
        testPantry.addIngredientList(testApple);
        testPantry.addIngredientList(testOrange);
        testPantry.addIngredientList(testCoke);
        testPantry.removeIngredientList(testApple);
        assertEquals(2, testPantry.getListOfAllIngredients().size());
    }

    @Test
    void testRemoveIngredientTwice() {
        testPantry.addIngredientList(testApple);
        testPantry.addIngredientList(testOrange);
        testPantry.addIngredientList(testCoke);
        testPantry.removeIngredientList(testApple);
        testPantry.removeIngredientList(testOrange);
        assertEquals(1, testPantry.getListOfAllIngredients().size());
    }

    @Test
    void testTotExpiredIngredientsNone() {
        testPantry.addIngredientList(testOrange);
        assertEquals(0, testPantry.totExpiredIngredients(LocalDate.of(2024, 2, 28)).size());
    }

    @Test
    void testTotExpiredIngredientsOne() {
        Purchase testOrangePurchase = new Purchase(4, LocalDate.of(2024, 2, 28));
        testOrange.addPurchase(testOrangePurchase);
        testPantry.addIngredientList(testOrange);
        assertEquals(1, testPantry.totExpiredIngredients(LocalDate.of(2024, 2, 29)).size());
    }

    @Test
    void testTotExpiredIngredientsOneNotExpired() {
        Purchase testOrangePurchase = new Purchase(4, LocalDate.of(2024, 2, 28));
        testOrange.addPurchase(testOrangePurchase);
        testPantry.addIngredientList(testOrange);
        assertEquals(0, testPantry.totExpiredIngredients(LocalDate.of(2024, 2, 27)).size());
    }

    @Test
    void testTotExpiredIngredientsMultipleNone() {
        Purchase testOrangePurchase = new Purchase(4, LocalDate.of(2024, 2, 28));
        testOrange.addPurchase(testOrangePurchase);
        Purchase testApplePurchase = new Purchase(4, LocalDate.of(2024, 2, 28));
        testApple.addPurchase(testApplePurchase);
        testPantry.addIngredientList(testOrange);
        testPantry.addIngredientList(testApple);
        assertEquals(0, testPantry.totExpiredIngredients(LocalDate.of(2024, 2, 27)).size());
    }

    @Test
    void testTotExpiredIngredientsMultipleOne() {
        Purchase testOrangePurchase = new Purchase(4, LocalDate.of(2024, 2, 28));
        testOrange.addPurchase(testOrangePurchase);
        Purchase testApplePurchase = new Purchase(4, LocalDate.of(2024, 2, 26));
        testApple.addPurchase(testApplePurchase);
        testPantry.addIngredientList(testOrange);
        testPantry.addIngredientList(testApple);
        assertEquals(1, testPantry.totExpiredIngredients(LocalDate.of(2024, 2, 27)).size());
    }

    @Test
    void testTotExpiredIngredientsMultipleAll() {
        Purchase testOrangePurchase = new Purchase(4, LocalDate.of(2024, 2, 28));
        testOrange.addPurchase(testOrangePurchase);
        Purchase testApplePurchase = new Purchase(4, LocalDate.of(2024, 2, 28));
        testApple.addPurchase(testApplePurchase);
        testPantry.addIngredientList(testOrange);
        testPantry.addIngredientList(testApple);
        assertEquals(2, testPantry.totExpiredIngredients(LocalDate.of(2024, 2, 29)).size());
    }

    @Test
    void testRemoveAllExpiredFoodNone() {
        Purchase testOrangePurchase = new Purchase(4, LocalDate.of(2024, 2, 28));
        testOrange.addPurchase(testOrangePurchase);
        testPantry.addIngredientList(testOrange);
        testPantry.removeAllExpiredFood(LocalDate.of(2024, 2, 27));
        assertEquals(1, testOrange.getListOfPurchase().size());
    }

    @Test
    void testRemoveAllExpiredFoodOne() {
        Purchase testOrangePurchase = new Purchase(4, LocalDate.of(2024, 2, 28));
        testOrange.addPurchase(testOrangePurchase);
        testPantry.addIngredientList(testOrange);
        testPantry.removeAllExpiredFood(LocalDate.of(2024, 2, 28));
        assertEquals(0, testOrange.getListOfPurchase().size());
    }

    @Test
    void testRemoveAllExpiredFoodSizeLargerSize() {
        Purchase testOrangePurchase = new Purchase(4, LocalDate.of(2024, 2, 28));
        Purchase testOrangePurchase2 = new Purchase(4, LocalDate.of(2024, 2, 14));
        testOrange.addPurchase(testOrangePurchase);
        testOrange.addPurchase(testOrangePurchase2);
        testPantry.addIngredientList(testOrange);
        Purchase testApplePurchase = new Purchase(4, LocalDate.of(2024, 2, 26));
        Purchase testApplePurchase2 = new Purchase(4, LocalDate.of(2024, 2, 29));
        testApple.addPurchase(testApplePurchase);
        testApple.addPurchase(testApplePurchase2);
        testPantry.addIngredientList(testApple);

        testPantry.removeAllExpiredFood(LocalDate.of(2024, 2, 27));
        assertEquals(1, testOrange.getListOfPurchase().size());
        assertEquals(1, testApple.getListOfPurchase().size());
    }


}
