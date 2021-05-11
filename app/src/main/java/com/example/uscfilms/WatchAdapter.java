package com.example.uscfilms;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

public class WatchAdapter extends RecyclerView.Adapter<WatchAdapter.ViewHolder> {

    private ArrayList<CardData> localDataSet;
    Context context;
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final ImageButton imageButton;
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            imageView = (ImageView) view.findViewById(R.id.watchImage);
            imageButton = (ImageButton) view.findViewById(R.id.watchButton);
            textView = (TextView) view.findViewById(R.id.watchText);
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageButton getImageButton() {
            return imageButton;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public WatchAdapter(ArrayList<CardData> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.watch_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        CardData cardItem = localDataSet.get(position);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        Glide.with(viewHolder.itemView)
                .load(cardItem.getImgUrl())
                .fitCenter()
                .into(viewHolder.imageView);

        if (cardItem.getType().equals("tv")) viewHolder.textView.setText("TV");
        else viewHolder.textView.setText("Movie");

        // onClick
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = cardItem.getId();
                System.out.println("clicks on image " + id);
                // get data and open detail page
                Utils.openDetailActivity(context, cardItem.getType(), cardItem.getId().toString());
            }
        });
        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.removeFromList(cardItem);
                if (Utils.getList().size() == 0) {
                    Utils.setEmptyWatchlist();
                } else {
                    Utils.resetAdapterWatch(context, Utils.getList());
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }



    public void openTMDB(String type, String id) {
        System.out.println("Open in TMDB");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/" + type + "/" + id));
        context.startActivity(browserIntent);
    }

}
