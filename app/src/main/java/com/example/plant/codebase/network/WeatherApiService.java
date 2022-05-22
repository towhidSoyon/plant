package com.example.plant.codebase.network;

import com.example.plant.codebase.network.model.MyWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("data/2.5/weather?q=Dhaka&appid=9b3a9be1a08df05a7e3d751e6346f0bc&units=metric")
    Call<MyWeather> getApiResponse();



}
