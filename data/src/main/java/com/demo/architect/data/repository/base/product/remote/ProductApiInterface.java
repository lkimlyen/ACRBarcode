package com.demo.architect.data.repository.base.product.remote;


import com.demo.architect.data.BaseListResponse;
import com.demo.architect.data.model.ProductEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Skull on 04/01/2018.
 */

public interface ProductApiInterface {
    @GET("http://acctest.imark.com.vn//WS/api/GetAllDetailForSOACR")
    Call<BaseListResponse<ProductEntity>> getAllDetailForSOACR(@Query("pLenhSXID") int requestId);



}
