package com.riza.ecommerce.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.riza.ecommerce.data.remote.api.ApiClient;
import com.riza.ecommerce.data.remote.response.ProductResponse;
import com.riza.ecommerce.domain.model.Product;
import com.riza.ecommerce.utils.Resource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    public LiveData<Resource<List<Product>>> getProducts(
            int limit,
            int skip,
            boolean error
    ) {
        MutableLiveData<Resource<List<Product>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));
        if (error) {
            result.postValue(
                    Resource.error("500 Internal Server Error", null)
            );
            return result;
        }

        ApiClient.getProductApi()
                .getProducts(limit, skip)
                .enqueue(new Callback<ProductResponse>() {

                    @Override
                    public void onResponse(
                            Call<ProductResponse> call,
                            Response<ProductResponse> response
                    ) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Product> products = new ArrayList<>();

                            for (ProductResponse.ProductDto dto :
                                    response.body().getProducts()) {

                                products.add(new Product(
                                        dto.getId(),
                                        dto.getTitle(),
                                        dto.getDescription(),
                                        dto.getPrice(),
                                        dto.getThumbnail(),
                                        dto.getStock(),
                                        dto.getCategory()
                                ));
                            }

                            result.setValue(Resource.success(products));
                        } else {
                            result.setValue(
                                    Resource.error(
                                            "Error " + response.code(),
                                            null
                                    )
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<ProductResponse> call,
                            Throwable t
                    ) {
                        result.setValue(
                                Resource.error(t.getMessage(), null)
                        );
                    }
                });

        return result;
    }


    public LiveData<Resource<Product>> getProductById(int id) {
        MutableLiveData<Resource<Product>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        ApiClient.getProductApi().getProductById(id).enqueue(new Callback<ProductResponse.ProductDto>() {
            @Override
            public void onResponse(Call<ProductResponse.ProductDto> call, Response<ProductResponse.ProductDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductResponse.ProductDto dto = response.body();
                    Product product = new Product(
                            dto.getId(),
                            dto.getTitle(),
                            dto.getDescription(),
                            dto.getPrice(),
                            dto.getThumbnail(),
                            dto.getStock(),
                            dto.getCategory()
                    );
                    result.setValue(Resource.success(product));
                } else {
                    result.setValue(Resource.error("Failed to load product", null));
                }
            }

            @Override
            public void onFailure(Call<ProductResponse.ProductDto> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }
}
