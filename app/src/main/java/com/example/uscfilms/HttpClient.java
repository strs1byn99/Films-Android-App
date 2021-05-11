package com.example.uscfilms;


import android.util.Log;/**/
import android.content.Context;
import android.view.View;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HttpClient {
    private static final String TAG = "HttpClient";

    static RequestQueue mQueue = null;

//    public static String DOMAIN = "http://10.0.2.2:8000";
    public static String DOMAIN = "http://tmdb-android-env.eba-jgku5idy.us-west-1.elasticbeanstalk.com";

    public static RequestQueue getRequestQueue(Context context) {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(context);
        }
        return mQueue;
    }

}
