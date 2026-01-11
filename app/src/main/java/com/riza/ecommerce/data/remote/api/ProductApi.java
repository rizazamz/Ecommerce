package com.riza.ecommerce.data.remote.api;

import com.riza.ecommerce.data.remote.response.ProductResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductApi {
    @GET("products")
    Call<ProductResponse> getProducts(
            @Query("limit") int limit,
            @Query("skip") int skip
    );

    @GET("products/{id}")
    Call<ProductResponse.ProductDto> getProductById(@Path("id") int id);
}