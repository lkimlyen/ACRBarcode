package com.demo.architect.data.repository.base.product.remote;


import com.demo.architect.data.model.BaseListResponse;
import com.demo.architect.data.model.ProductEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Skull on 04/01/2018.
 */

public interface ProductApiInterface {
//    @GET("/WS/api/GetAllDetailForSOACR")
//    Call<BaseListResponse<ProductEntity>> getAllDetailForSOACR(@Query("pLenhSXID") int requestId);

    @GET
    Call<BaseListResponse<ProductEntity>> getAllDetailForSOACR(@Url String url, @Query("pLenhSXID") int requestId);

}
