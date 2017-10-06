package com.therisingtechie.geello.api;

import com.therisingtechie.geello.model.UserDataResponse;
import com.therisingtechie.geello.request.LoginRequest;
import com.therisingtechie.geello.request.UserSimpleRegistration;
import com.therisingtechie.geello.request.UserSocialRegistration;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by chiranjeevi mateti on 03-Oct-17.
 */

public interface ApiInterface {

    @POST("api/register")
    Call<UserDataResponse> sendRegistrationDetails(@Body UserSimpleRegistration log_out);


    @POST("api/register")
    Call<UserDataResponse> sendSocialRegistrationDetails(@Body UserSocialRegistration log_out);


    @POST("api/login")
    Call<UserDataResponse> sendLoginDetails(@Body LoginRequest log_out);





    //@Header("Accept-Language") String lang

    //@POST("/Driver_Log_Out")
    //void Driver_Log_Out(@Body DriverLogoutParameter log_out, Callback<CommonResult> callback);


}
