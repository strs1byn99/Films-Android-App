package com.example.uscfilms;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

public class Utils {

     public static String defaultVideoLink = "tzkWB85ULJY";
     public static String watchList = "MyWatchlist";
     public static SharedPreferences SP;
     static RecyclerView watchRecyclerView;
     static ScrollView watchScrollView;
     static TextView watchTextView;
     static Context BaseContext;

    public static void setBaseContext(Context baseContext) {
        BaseContext = baseContext;
    }

    public static void setWatchScrollView(ScrollView watchScrollView) {
        Utils.watchScrollView = watchScrollView;
    }

    public static void setWatchTextView(TextView watchTextView) {
        Utils.watchTextView = watchTextView;
    }

    public static void setWatchRecyclerView(RecyclerView watchRecyclerView) {
        Utils.watchRecyclerView = watchRecyclerView;
    }

    public static void openDetailActivity(Context context, String type, String id) {
        Intent i = new Intent(context, DetailActivity.class);
        i.putExtra("type", type);
        i.putExtra("id", id);
        context.startActivity(i);
    }

    public static void openReviewActivity(Context context, String by, String rating, String content) {
         Intent i = new Intent(context, ReviewActivity.class);
         i.putExtra("by", by);
         i.putExtra("rating", rating);
         i.putExtra("content", content);
         context.startActivity(i);
    }

    public static void openTwitter(Context context, String type, String id) {
        String tmdb_url = "https://www.themoviedb.org/" + type + "/" + id;
        String url = "https://twitter.com/intent/tweet?text=Check this out!%0A" + tmdb_url;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static void openFacebook(Context context, String type, String id) {
        String tmdb_url = "https://www.themoviedb.org/" + type + "/" + id;
        String url = "https://www.facebook.com/sharer/sharer.php?u=" + tmdb_url;
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static void setSP(SharedPreferences SP) {
        Utils.SP = SP;
    }

    public static void addToList(CardData cardData) {
        System.out.println("Adding..." + cardData.getTitle());
        String json = SP.getString(Utils.watchList, new JSONArray().toString());
        JSONArray jarray;
        try {
            jarray = new JSONArray(json);
            String obj = new Gson().toJson(cardData);
            JSONObject jo = new JSONObject(obj);
            jarray.put(jo);
            updateSP(SP.edit(), jarray);
            Toast.makeText(Utils.BaseContext, cardData.getTitle() + " was added to Watchlist", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(SP.getAll());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    public static void removeFromList(CardData cardData) {
        System.out.println("Removing..." + cardData.getTitle());
        String json = SP.getString(Utils.watchList, new JSONArray().toString());
        JSONArray jarray;
        try {
            jarray = new JSONArray(json);
            int idx = 0;
            for (int i = 0, len = jarray.length(); i < len; i += 1) {
                JSONObject obj = jarray.getJSONObject(i);
                if (cardData.getId() == obj.getInt("id") && cardData.getType().equals(obj.getString("type"))) {
                    idx = i; break;
                }
            }
            jarray.remove(idx);
            Utils.updateSP(SP.edit(), jarray);
            Toast.makeText(Utils.BaseContext, cardData.getTitle() + " was removed from Watchlist", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(SP.getAll());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    public static boolean checkIfAdded(CardData cardData) {
        String json = SP.getString(Utils.watchList, new JSONArray().toString());
        JSONArray jarray;
        try {
            jarray = new JSONArray(json);
            for (int i = 0, len = jarray.length(); i < len; i += 1) { ;
                JSONObject obj = jarray.getJSONObject(i);
                if (cardData.getId() == obj.getInt("id") && cardData.getType().equals(obj.getString("type"))) {
                    System.out.println(cardData.getTitle() + "is added already!!!");
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(cardData.getTitle() + "is NOT added");
        return false;
    }

    private static void updateSP(SharedPreferences.Editor e, JSONArray jarray) {
         e.putString(Utils.watchList, jarray.toString());
         e.commit();
    }

    public static void resetAdapterWatch(Context context, ArrayList<CardData> array) {
        WatchAdapter adapter = new WatchAdapter(array);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        watchRecyclerView.setLayoutManager(layoutManager);
        watchRecyclerView.setItemAnimator(new DefaultItemAnimator());
        watchRecyclerView.setAdapter(adapter);
    }

    public static ArrayList<CardData> getList() {
        ArrayList<CardData> array = new ArrayList<>();
        try {
            String j = Utils.SP.getString(Utils.watchList, new JSONArray().toString());
            JSONArray jarray = new JSONArray(j);
            for (int i = 0, len = jarray.length(); i < len; i += 1) {
                JSONObject obj = jarray.getJSONObject(i);
                CardData cardData = new CardData(obj.getString("imgUrl"),
                        Integer.valueOf(obj.getString("id")), obj.getString("type"), obj.getString("title"));
                array.add(cardData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(array);
        return array;
    }

    public static void setEmptyWatchlist() {
        Utils.watchScrollView.setVisibility(View.GONE);
        Utils.watchTextView.setVisibility(View.VISIBLE);
    }

    public static void swap(int from, int to) {
        ArrayList<CardData> array = getList();
        CardData movedCard = array.get(from);
        array.remove(movedCard);
        array.add(to, movedCard);
        JSONArray jarray = toJsonArray(array);
        updateSP(SP.edit(), jarray);
    }

    public static JSONArray toJsonArray(ArrayList<CardData> array) {
        Gson gson = new Gson();
        JSONArray jarray = new JSONArray();
        try {
            for (int i = 0, len = array.size(); i < len; i += 1) {
                String obj = gson.toJson(array.get(i));
                JSONObject jo = new JSONObject(obj);
                jarray.put(jo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jarray;
    }
}
