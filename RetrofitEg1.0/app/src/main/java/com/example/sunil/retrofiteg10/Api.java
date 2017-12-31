package com.example.sunil.retrofiteg10;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Sunil on 12/28/2017.
 */

public interface Api {

    String BASE_URL="https://simplifiedcoding.net/demos/";
    //String BASE_URL="https://api.github.com/";

    @GET("marvel")
    Call <ArrayList<Hero>> getHeros();
}
