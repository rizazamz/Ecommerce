package com.riza.ecommerce.presentation.cart;

import android.app.Application;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import com.riza.ecommerce.data.local.entity.CartItem;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class CartViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private Application application;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test Case 3: Ubah qty menghasilkan total yang benar
     */
    @Test
    public void testCartItemTotalPrice_CalculationCorrect() {
        // Given: Product $10, quantity 1
        CartItem item = new CartItem(1, "Test Product", 10.0, "url", 1, 100);
        assertEquals("Initial total should be $10", 10.0, item.getTotalPrice(), 0.01);

        // When: Quantity jadi 3
        item.setQuantity(3);

        // Then: Total $30
        assertEquals("Total should be $30 after quantity change", 30.0, item.getTotalPrice(), 0.01);
    }

    @Test
    public void testCartItemTotalPrice_WithMultipleItems() {
        CartItem item1 = new CartItem(1, "Product 1", 10.0, "url", 2, 100);
        CartItem item2 = new CartItem(2, "Product 2", 15.0, "url", 3, 100);

        double total = item1.getTotalPrice() + item2.getTotalPrice();

        // (10 * 2) + (15 * 3) = 20 + 45 = 65
        assertEquals("Total should be $65", 65.0, total, 0.01);
    }

    @Test
    public void testCartItemTotalWithShipping() {
        CartItem item = new CartItem(1, "Product", 20.0, "url", 2, 100);
        double subtotal = item.getTotalPrice();
        double shipping = 10.0;
        double total = subtotal + shipping;

        // (20 * 2) + 10 = 50
        assertEquals("Total with shipping should be $50", 50.0, total, 0.01);
    }

    @Test
    public void testUpdateQuantity_WithinStockLimit() {
        CartItem item = new CartItem(1, "Product", 10.0, "url", 1, 5);

        // Update quantity to 3 (within stock of 5)
        item.setQuantity(3);
        assertEquals(3, item.getQuantity());

        // Should not exceed stock
        assertTrue("Quantity should not exceed stock", item.getQuantity() <= item.getStock());
    }
}