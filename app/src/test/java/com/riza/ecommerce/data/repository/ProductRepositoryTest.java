package com.riza.ecommerce.data.repository;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import com.riza.ecommerce.domain.model.Product;
import com.riza.ecommerce.utils.Resource;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ProductRepositoryTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private ProductRepository repository;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        repository = new ProductRepository();
    }

    @Test
    public void testGetProducts_ReturnsNonNull() {
        LiveData<Resource<List<Product>>> result = repository.getProducts(10, 0,false);
        assertNotNull("Product LiveData should not be null", result);
    }

    @Test
    public void testGetProductById_ReturnsNonNull() {
        LiveData<Resource<Product>> result = repository.getProductById(1);
        assertNotNull("Product detail LiveData should not be null", result);
    }

    @Test
    public void testPagination_NoDuplication() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        LiveData<Resource<List<Product>>> result = repository.getProducts(10, 0,false);

        result.observeForever(new Observer<Resource<List<Product>>>() {
            @Override
            public void onChanged(Resource<List<Product>> resource) {
                if (resource.getStatus() == Resource.Status.SUCCESS && resource.getData() != null) {
                    List<Product> products = resource.getData();
                    assertTrue("Products should not be empty", products.size() > 0);

                    long uniqueIds = products.stream()
                            .map(Product::getId)
                            .distinct()
                            .count();

                    assertEquals("No duplicate products", products.size(), uniqueIds);
                    latch.countDown();
                }
            }
        });

        latch.await(10, TimeUnit.SECONDS);
    }
}