package com.example.sunil.retrofiteg;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sunil on 12/26/2017.
 */

public class ApiClient {

    public static final String Base_URL="https://api.github.com/";
    private static Retrofit retrofit=null;

    public static Retrofit getClient(){

        if(retrofit==null)
            retrofit=new Retrofit.Builder().baseUrl(Base_URL).addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit;
    }

}
