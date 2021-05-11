package com.example.uscfilms.ui.dashboard;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.uscfilms.CardAdapter;
import com.example.uscfilms.CardData;
import com.example.uscfilms.HttpClient;
import com.example.uscfilms.R;
import com.example.uscfilms.SearchAdapter;
import com.example.uscfilms.SearchData;
import com.example.uscfilms.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    View root;
    Context context;
    RecyclerView recyclerView;
    TextView text;
    ScrollView scrollView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        context = getContext();
        recyclerView = root.findViewById(R.id.recyclerView_search);
        scrollView = root.findViewById(R.id.hasResult);
        text = root.findViewById(R.id.no_result);
        text.setVisibility(View.GONE);

        SearchView sv = root.findViewById(R.id.search_view);
        sv.requestFocus();

        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty() || newText.trim().length() == 0) return false;
                if (newText.startsWith("&")) return false;
                JsonArrayRequest request = generateRequest(newText);
                HttpClient.getRequestQueue(context).add(request);
                return false;
            }
        });

        ImageView closeButton = (ImageView)root.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) root.findViewById(R.id.search_src_text);
                et.setText("");
                text.setVisibility(View.GONE);
                scrollView.setVisibility(View.GONE);
            }
        });

        return root;
    }

    private void noResult() {
        scrollView.setVisibility(View.GONE);
        text.setVisibility(View.VISIBLE);
    }

    private JsonArrayRequest generateRequest(String query) {
        String uri = String.format("/search?query=%1$s", query);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, HttpClient.DOMAIN + uri, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if (response.length() == 0) {
                        noResult();
                        return;
                    }
                    scrollView.setVisibility(View.VISIBLE);
                    text.setVisibility(View.GONE);
                    ArrayList<SearchData> results = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        String type = obj.getString("type");
                        Integer id = obj.getInt("id");
                        String rating = obj.getString("rating");
                        String year = obj.getString("year");
                        String title = obj.getString("title");
                        String img = obj.getString("img");
                        results.add(new SearchData(type, year, title, rating, id, img));
                        setRecyclerAdapter(results, recyclerView);
                    }
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
        return request;
    }

    private void setRecyclerAdapter(ArrayList<SearchData> array, RecyclerView recyclerView) {
        SearchAdapter adapter = new SearchAdapter(array);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }
}