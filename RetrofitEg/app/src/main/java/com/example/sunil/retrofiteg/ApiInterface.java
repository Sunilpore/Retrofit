package com.example.sunil.retrofiteg;


import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Sunil on 12/26/2017.
 */

public interface ApiInterface {

    @GET("users/Sunilpore")
    Call<UserModel> getUserData();

}
