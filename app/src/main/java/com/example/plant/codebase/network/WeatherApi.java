package com.example.plant.codebase.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApi {

    private  static  String BASE_URL = "https://api.openweathermap.org/";

    private   static  Retrofit retrofit ;


    public  static  Retrofit getInstance(){

        if (retrofit==null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;

    }



}

