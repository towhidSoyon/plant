package com.example.plant.codebase.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.example.plant.R;
import com.example.plant.codebase.activity.MainActivity;

import java.util.Locale;

public class LanguageManager {

    private Context ct;
    private SharedPreferences sharedPreferences;

    private Activity activity;
    public LanguageManager(Context ctx) {
        ct = ctx;
        sharedPreferences = ct.getSharedPreferences("LANG",Context.MODE_PRIVATE);
    }

    public void updateResource(String code) {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources resources = ct.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
       // resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        setLang(code);

    }


    public  void  recreateActivity(Activity activity){
        activity.recreate();
    }

    public String getLang(){
        return sharedPreferences.getString("lang","en");
    }

    public void setLang(String code){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lang",code);
        editor.commit();
    }
}
