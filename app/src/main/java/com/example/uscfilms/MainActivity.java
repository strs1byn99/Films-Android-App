package com.example.uscfilms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.uscfilms.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_USCFilms);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupWithNavController(navView, navController);

        SharedPreferences sp = getApplicationContext().getSharedPreferences(Utils.watchList, 0);
        Utils.setSP(sp);
        SharedPreferences.Editor e = sp.edit();
        // for testing
        // clear(e);
        String json = sp.getString(Utils.watchList, "");
        if (json.equals("")) {
            System.out.println("Empty list at the beginning...");
            JSONArray jarray = new JSONArray();
            e.putString(Utils.watchList, jarray.toString());
        } else {
            System.out.println("Watchlist is NOT empty...");
        }

        Utils.setBaseContext(MainActivity.this);
    }

    private void clear(SharedPreferences.Editor e) {
        e.clear();
        e.commit();
    }

}