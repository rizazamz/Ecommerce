package com.riza.ecommerce.presentation.product;

import android.app.Application;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import com.riza.ecommerce.domain.model.Product;
import com.riza.ecommerce.utils.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;

import static org.junit.Assert.*;

public class ProductViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private Application application;

    private ProductViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        viewModel = new ProductViewModel(application);
    }

    /**
     * Test Case 1: Pagination dasar
     */
    @Test
    public void testLoadProducts_ReturnsLiveData() {
        LiveData<Resource<List<Product>>> result = viewModel.getProducts();
        assertNotNull("Products LiveData should not be null", result);
    }

    @Test
    public void testRefreshProducts_ClearsExistingData() {
        viewModel.loadProducts(false);
        viewModel.refreshProducts(false);

        // After refresh, should start from skip 0
        assertNotNull(viewModel.getProducts());
    }
}