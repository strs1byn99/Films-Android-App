package com.example.uscfilms.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uscfilms.CardData;
import com.example.uscfilms.DetailActivity;
import com.example.uscfilms.R;
import com.example.uscfilms.ReccAdapter;
import com.example.uscfilms.Utils;
import com.example.uscfilms.WatchAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    View root;
    Context context;
    ArrayList<CardData> array = new ArrayList<>();
    RecyclerView recyclerView;
    ItemTouchHelper itemTouchHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        root = inflater.inflate(R.layout.fragment_notifications, container, false);
        context = getContext();


        TextView emptytext = root.findViewById(R.id.emptyWatchlist);
        ScrollView scrollView = root.findViewById(R.id.watchlist);
        recyclerView = root.findViewById(R.id.recyclerView_watch);
        Utils.setWatchRecyclerView(recyclerView);
        Utils.setWatchScrollView(scrollView);
        Utils.setWatchTextView(emptytext);
        itemTouchHelper = new ItemTouchHelper(simpleCallback);

        if (Utils.getList().size() == 0) {
            emptytext.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            emptytext.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            array = Utils.getList();
            Utils.resetAdapterWatch(context, array);
        }


        itemTouchHelper.attachToRecyclerView(recyclerView);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<CardData> arr = Utils.getList();
        if (arr.size() == 0) {
            Utils.setEmptyWatchlist();
        } else {
            Utils.resetAdapterWatch(context, arr);
        }
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            // swap position
            Utils.swap(fromPosition, toPosition);
            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            // recyclerView.setAdapter(new WatchAdapter(Utils.getList()));
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

}