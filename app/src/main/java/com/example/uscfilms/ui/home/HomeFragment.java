package com.example.uscfilms.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.uscfilms.CardAdapter;
import com.example.uscfilms.CardData;
import com.example.uscfilms.HttpClient;
import com.example.uscfilms.MainActivity;
import com.example.uscfilms.R;
import com.example.uscfilms.SliderAdapter;
import com.example.uscfilms.SliderData;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private View root;
    ConstraintLayout movieLayout;
    ConstraintLayout tvLayout;
    Context thiscontext;
    static JSONArray slidersMv = new JSONArray();
    static JSONArray slidersTv = new JSONArray();
    static JSONArray popMv;
    static JSONArray popTv;
    static JSONArray topMv;
    static JSONArray topTv;
    public String BASE_URL = HttpClient.DOMAIN;
    private RecyclerView recyclerViewPopMv;
    private RecyclerView recyclerViewTopMv;
    private RecyclerView recyclerViewPopTv;
    private RecyclerView recyclerViewTopTv;
    ArrayList<SliderData> sliderDataArrayListMv;
    ArrayList<SliderData> sliderDataArrayListTv;
    SliderView tvSlider;
    SliderView mvSlider;
    LinearLayout loading;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        thiscontext = container.getContext();
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        movieLayout = root.findViewById(R.id.movie_content);
        tvLayout = root.findViewById(R.id.tv_content);
        tvLayout.setVisibility(LinearLayout.GONE);
        tvSlider = root.findViewById(R.id.slider2);
        mvSlider = root.findViewById(R.id.slider);
        loading = root.findViewById(R.id.home_loading);
        ScrollView scrollView = root.findViewById(R.id.home_scrollview);

        // switch tabs
        RadioGroup rb = root.findViewById(R.id.tabs);
        rb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.movie_tab:
                        scrollView.smoothScrollTo(0,0);
                        tvLayout.setVisibility(LinearLayout.GONE);
                        movieLayout.setVisibility(LinearLayout.VISIBLE);
                        // to task
                        break;
                    case R.id.tv_tab:
                        scrollView.smoothScrollTo(0,0);
                        movieLayout.setVisibility(LinearLayout.GONE);
                        tvLayout.setVisibility(LinearLayout.VISIBLE);

                        // to task
                        break;
                }
            }

        });


        setOnClickOpenTMDB();

        getPlaying();
        getHome();

        recyclerViewTopMv = root.findViewById(R.id.recyclerView_topmv);
        recyclerViewPopMv = root.findViewById(R.id.recyclerView_popmv);
        recyclerViewTopTv = root.findViewById(R.id.recyclerView_toptv);
        recyclerViewPopTv = root.findViewById(R.id.recyclerView_poptv);


        return root;
    }

    private void setRecyclerAdapter(ArrayList<CardData> array, RecyclerView recyclerView) {
        CardAdapter adapter = new CardAdapter(array);
        LinearLayoutManager layoutManager = new LinearLayoutManager(thiscontext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void getPlaying() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_URL+"/playing", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    slidersMv = response.getJSONArray("mv");
                    slidersTv = response.getJSONArray("tv");
                    sliderDataArrayListMv = new ArrayList<>();
                    sliderDataArrayListTv = new ArrayList<>();
                    for (int i = 0, size = slidersMv.length(); i < size; i++) {
                        JSONObject objectInArray = slidersMv.getJSONObject(i);
                        JSONObject objectInArray2 = slidersTv.getJSONObject(i);
                        sliderDataArrayListMv.add(new SliderData((String) objectInArray.get("img"),
                                (Integer) objectInArray.get("id"),
                                "movie"));
                        sliderDataArrayListTv.add(new SliderData((String) objectInArray2.get("img"),
                                (Integer) objectInArray2.get("id"),
                                "tv"));
                    }
                    showSlides(sliderDataArrayListMv, mvSlider);
                    showSlides(sliderDataArrayListTv, tvSlider);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        HttpClient.getRequestQueue(thiscontext).add(request);
    }

    private void setData(JSONArray data, RecyclerView recyclerView) throws JSONException {
        ArrayList<CardData> cardDataArrayList = new ArrayList<>();
        for (int i = 0, size = data.length(); i < size; i++) {
            JSONObject objectInArray = data.getJSONObject(i);
            String img = (String) objectInArray.get("img");
            Integer id = (Integer) objectInArray.get("id");
            String type = (String) objectInArray.get("type");
            String title = (String) objectInArray.get("title");
            cardDataArrayList.add(new CardData(img, id, type, title));
        }
        setRecyclerAdapter(cardDataArrayList, recyclerView);
    }

    public void getHome() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_URL+"/home", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    popMv = response.getJSONArray("popmv");
                    popTv = response.getJSONArray("poptv");
                    topMv = response.getJSONArray("topmv");
                    topTv = response.getJSONArray("toptv");
                    setData(topMv, recyclerViewTopMv);
                    setData(popMv, recyclerViewPopMv);
                    setData(topTv, recyclerViewTopTv);
                    setData(popTv, recyclerViewPopTv);
                    loading.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        HttpClient.getRequestQueue(thiscontext).add(request);
    }

    public void showSlides(ArrayList<SliderData> sliderDataArrayList, SliderView sliderView) {

        // passing this array list inside our adapter class.
        SliderAdapter adapter = new SliderAdapter(thiscontext, sliderDataArrayList);

        // below method is used to set auto cycle direction in left to
        // right direction you can change according to requirement.
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

        // below method is used to
        // setadapter to sliderview.
        sliderView.setSliderAdapter(adapter);

        // below method is use to set
        // scroll time in seconds.
        sliderView.setScrollTimeInSec(3);

        // to set it scrollable automatically
        // we use below method.
        sliderView.setAutoCycle(true);

        // to start autocycle below method is used.
        sliderView.startAutoCycle();
    }

    public void setOnClickOpenTMDB() {
        TextView ft = root.findViewById(R.id.footer1);
        TextView ft2 = root.findViewById(R.id.footer2);
        System.out.println("setOnClickOpenTMDB");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/"));
        ft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(browserIntent);
            }
        });
        ft2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(browserIntent);
            }
        });

    }

}