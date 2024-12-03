package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TestPurchase {
    Purchase testPurchase1;

    @BeforeEach
    public void setUpTestPurchase() {
        testPurchase1 = new Purchase(4, LocalDate.of(2024, 2, 28));
    }

    @Test
    void testGetAmount() {
        assertEquals(4, testPurchase1.getAmount());
    }

    @Test
    void testGetExpirationDate() {
        LocalDate date = LocalDate.of(2024, 2, 28);
        assertEquals(date, testPurchase1.getExpirationDate());
    }

    @Test
    void testConstructor() {
        assertEquals(4, testPurchase1.getAmount());
        assertEquals(LocalDate.of(2024, 2, 28), testPurchase1.getExpirationDate());
    }

    @Test
    void testRemoveAmountLess() {
        testPurchase1.removeAmount(1);
        assertEquals(3, testPurchase1.getAmount());
    }

    @Test
    void testRemoveAll() {
        testPurchase1.removeAmount(4);
        assertEquals(0, testPurchase1.getAmount());
    }

    @Test
    void testSetAmountOnce() {
        testPurchase1.setAmount(4);
        assertEquals(4, testPurchase1.getAmount());
    }

    @Test
    void testSetAmountMultipleTimes() {
        testPurchase1.setAmount(4);
        testPurchase1.setAmount(5);
        assertEquals(5, testPurchase1.getAmount());
    }

    @Test
    void testIsExpired() {
        assertFalse(testPurchase1.isExpired(LocalDate.of(2024, 2, 27)));
        assertTrue(testPurchase1.isExpired(LocalDate.of(2024, 2, 28)));
        assertTrue(testPurchase1.isExpired(LocalDate.of(2024, 2, 29)));
    }
}
