package com.digitalscale.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vishal Gadhiya on 3/8/2017.
 */

public class ApiClient {

    //public static final String BASE_URL = "http://192.168.1.7/digital-scale/public/api/"; // Local
    public static final String BASE_URL = "http://local.websoptimization.com:83/digital-scale-web/public/api/";// live

    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
