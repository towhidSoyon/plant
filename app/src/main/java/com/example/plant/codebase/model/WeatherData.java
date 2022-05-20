package com.example.plant.codebase.model;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherData {

    private String mTemperature;
    private String mIcon;
    private String mCity;
    private String mWeatherType;
    private int mCondition;

    public static WeatherData fromJson(JSONObject jsonObject){
        try {
            WeatherData weatherD = new WeatherData();
            weatherD.mCity = jsonObject.getString("name");
            weatherD.mCondition = jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mWeatherType = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.mIcon = updateWeatherIcon(weatherD.mCondition);
            double tempResult = jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue = (int) Math.rint(tempResult);
            weatherD.mTemperature = Integer.toString(roundedValue);
            return weatherD;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String updateWeatherIcon(int mCondition) {
        if (mCondition>= 0 && mCondition<300){
            return "thunderstorm";
        } else if (mCondition>=300 && mCondition<500){
            return "rainy";
        } else if (mCondition>=500 && mCondition<600){
            return "rainy";
        } else if (mCondition>=600 && mCondition<700){
            return "snow";
        } else if (mCondition>=701 && mCondition<771){
            return "fog";
        }  else if (mCondition>=772 && mCondition<800){
            return "overcast";
        } else if (mCondition == 800){
            return "sunny";
        }  else if (mCondition>=801 && mCondition<804){
            return "cloudy";
        } else if (mCondition>=900 && mCondition<902){
            return "thunderstorm";
        }
        if (mCondition==903){
            return "snow";
        }
        if (mCondition==904){
            return "sunny";
        }
        if (mCondition>=905 && mCondition<=1000){
            return "thunderstorm";
        }

        return "dunno";

    }

    public String getmTemperature() {
        return mTemperature+"°C";
    }

    public String getmIcon() {
        return mIcon;
    }

    public String getmCity() {
        return mCity;
    }

    public String getmWeatherType() {
        return mWeatherType;
    }
}
