package com.demo.architect.data.repository.base.account.remote;


import com.demo.architect.data.Def;
import com.demo.architect.data.model.BaseResponse;
import com.demo.architect.data.model.UpdateAppResponse;
import com.demo.architect.data.model.UserResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Skull on 04/01/2018.
 */

public interface AuthApiInterface {
      @FormUrlEncoded
    @POST("/WS/api/LoginWS")
    Call<UserResponse> login(@Field("pUserName") String username, @Field("pPassWord") String password,
                             @Field("pUserType") String type);

    @FormUrlEncoded
    @POST("/WS/api/ChangePassWord")
    Call<BaseResponse> changePassWord(@Field("pUserID") String userId, @Field("pOldPass") String oldPass,
                            @Field("pNewPass") String newPass);

    @GET("/WS/api/GetUpdateVersionACR?pAppCode=ids")
    Call<UpdateAppResponse> getUpdateVersionACR();

    @GET("http://sp2t9.imark.com.vn/WS/api/GetDate?pAppCode=ids")
    Call<String> getDateServer();
}
