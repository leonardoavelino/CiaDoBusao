package com.example.snakechild.ciadobusao;

import android.app.Application;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;


public class SplashScreen extends Application {

    @Override
    public void onCreate() {
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "r2Hs81lOwoi7YK9mby5m49409JuOx5EzpBULMNnP", "tdeLsjWBSqTd5KRCoULEScSbzKHpW80m2bpHQZzZ");
        super.onCreate();
    }
}
