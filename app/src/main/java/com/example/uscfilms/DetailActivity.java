package com.example.uscfilms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    static JSONObject video;
    static JSONObject detail;
    static JSONArray review;
    static JSONArray cast;
    static JSONArray recc;
    Context context;
    ImageView backdropImage;
    YouTubePlayerView youTubePlayerView;
    String type;
    String id;
    ImageButton btn;
    LinearLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        context = DetailActivity.this;

        // getIntent() is a method from the started activity
        Intent myIntent = getIntent();
        type = myIntent.getStringExtra("type");
        id = myIntent.getStringExtra("id");
        System.out.println("the type I get from Intent: " + type);
        System.out.println("the id I get from Intent: " + id);

        getDetail(type, id);
        backdropImage = findViewById(R.id.backdrop);
        btn = findViewById(R.id.detail_addToList);
        loading = findViewById(R.id.detail_loading);

    }

    private void checkVideo() {
        try {
            String videoId = video.getString("link");
            youTubePlayerView = findViewById(R.id.youtube_player_view);
            if (videoId.equals(Utils.defaultVideoLink)) {
                youTubePlayerView.setVisibility(View.GONE);
                String img = detail.getString("backdrop");
                if (img == null) {
                    // set a default image if null
                } else {
                    Glide.with(context)
                            .load(detail.getString("backdrop"))
                            .fitCenter()
                            .into(backdropImage);
                }
            } else {
                backdropImage.setVisibility(View.GONE);
                setVideo();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setVideo() {
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = null;
                try {
                    videoId = video.getString("link");
                    youTubePlayer.cueVideo(videoId, 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setTitle() {
        TextView title = findViewById(R.id.title);
        try {
            title.setText(detail.getString("title"));
            title.setGravity(title.getLineCount() > 1? Gravity.CENTER : Gravity.LEFT);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setOverview() {
        TextView text = findViewById(R.id.overview);
        LinearLayout overview = findViewById(R.id.overview_layout);
        try {
            if (detail.getString("overview").equals("")) overview.setVisibility(View.GONE);
            else text.setText(detail.getString("overview"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setGenre() {
        TextView text = findViewById(R.id.genre);
        LinearLayout genre = findViewById(R.id.genre_layout);
        try {
            if (detail.getString("genres").equals("")) genre.setVisibility(View.GONE);
            else text.setText(detail.getString("genres"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setYear() {
        TextView text = findViewById(R.id.year);
        LinearLayout year = findViewById(R.id.year_layout);
        try {
            if (detail.getString("year").equals("")) year.setVisibility(View.GONE);
            else text.setText(detail.getString("year"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setCast() {
        LinearLayout cast_layout = findViewById(R.id.cast_layout);
        if (cast.length() == 0) {
            cast_layout.setVisibility(View.GONE); return;
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerView_cast);
        ArrayList<CastData> castDataArrayList = new ArrayList<>();
        for (int i = 0, size = cast.length(); i < size; i++) {
            JSONObject objectInArray = null;
            try {
                objectInArray = cast.getJSONObject(i);
                String img = (String) objectInArray.get("img");
                String name = (String) objectInArray.get("name");
                castDataArrayList.add(new CastData(img, name));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setRecyclerAdapterCast(castDataArrayList, recyclerView);
    }

    private void setReview() {
        LinearLayout review_layout = findViewById(R.id.review_layout);
        if (review.length() == 0) {
            review_layout.setVisibility(View.GONE); return;
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerView_review);
        ArrayList<ReviewData> reviewDataArrayList = new ArrayList<>();
        for (int i = 0, size = review.length(); i < size; i++) {
            JSONObject objectInArray = null;
            try {
                objectInArray = review.getJSONObject(i);
                String by = (String) objectInArray.get("by");
                String content = (String) objectInArray.get("content");
                String rating = (String) objectInArray.get("rating");
                reviewDataArrayList.add(new ReviewData(by, content, rating));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setRecyclerAdapterReview(reviewDataArrayList, recyclerView);
    }

    private void setRecc() {
        LinearLayout recc_layout_ = findViewById(R.id.recc_layout_);
        if (recc.length() == 0) {
            recc_layout_.setVisibility(View.GONE); return;
        }
        RecyclerView recyclerView = findViewById(R.id.recyclerView_recc);
        ArrayList<CardData> cardDataArrayList = new ArrayList<>();
        for (int i = 0, size = recc.length(); i < size; i++) {
            JSONObject objectInArray = null;
            try {
                objectInArray = recc.getJSONObject(i);
                String img = (String) objectInArray.get("img");
                Integer id = (Integer) objectInArray.get("id");
                String type = (String) objectInArray.get("type");
                String title = (String) objectInArray.get("title");
                cardDataArrayList.add(new CardData(img, id, type, title));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        setRecyclerAdapterRecc(cardDataArrayList, recyclerView);
    }

    private void setShare() {
        ImageButton tt = findViewById(R.id.detail_share_tt);
        ImageButton fb = findViewById(R.id.detail_share_fb);
        tt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openTwitter(context, type, id);
            }
        });
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openFacebook(context, type, id);
            }
        });
    }

    private void setDetail() {
//        setVideo();
        checkVideo();
        setTitle();
        setOverview();
        setGenre();
        setYear();
        setCast();
        setReview();
        setRecc();
        setShare();
        loading.setVisibility(View.GONE);
    }

    private void setBtn() throws JSONException {
        CardData cardData = new CardData(detail.getString("poster"), Integer.valueOf(id), type, detail.getString("title"));
        if (Utils.checkIfAdded(cardData)) {
            btn.setBackgroundResource(R.drawable.ic_baseline_remove_circle_outline_24);
        } else {
            btn.setBackgroundResource(R.drawable.ic_baseline_add_circle_outline_24);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.checkIfAdded(cardData)) {
                    btn.setBackgroundResource(R.drawable.ic_baseline_add_circle_outline_24);
                    Utils.removeFromList(cardData);
                } else {
                    btn.setBackgroundResource(R.drawable.ic_baseline_remove_circle_outline_24);
                    Utils.addToList(cardData);
                }

            }
        });
    }

    private void getDetail(String type, String id) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, HttpClient.DOMAIN+"/"+type+"/"+id,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    video = response.getJSONObject("video");
                    detail = response.getJSONObject("detail");
                    review = response.getJSONArray("review");
                    cast = response.getJSONArray("cast");
                    recc = response.getJSONArray("recc");
                    System.out.println(review);
                    setDetail();
                    setBtn();
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
        HttpClient.getRequestQueue(context).add(request);
    }

    private void setRecyclerAdapterRecc(ArrayList<CardData> array, RecyclerView recyclerView) {
        ReccAdapter adapter = new ReccAdapter(array);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setRecyclerAdapterReview(ArrayList<ReviewData> array, RecyclerView recyclerView) {
        ReviewAdapter adapter = new ReviewAdapter(array);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void setRecyclerAdapterCast(ArrayList<CastData> array, RecyclerView recyclerView) {
        CastAdapter adapter = new CastAdapter(array);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

}