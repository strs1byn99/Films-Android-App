package com.example.uscfilms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

//    private String[] localDataSet;
    private ArrayList<CardData> localDataSet;
    Context context;
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final ImageButton imageButton;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            imageView = (ImageView) view.findViewById(R.id.cardImage);
            imageButton = (ImageButton) view.findViewById(R.id.imageButton);
        }

        public ImageView getImageView() {
            return imageView;
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public CardAdapter(ArrayList<CardData> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);

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

        Drawable d = ContextCompat.getDrawable(context, R.drawable.gradient);
        viewHolder.imageView.setForeground(d);

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

        // popup
        viewHolder.imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String id = cardItem.getId().toString();
                String type = cardItem.getType();
                PopupMenu popupMenu = new PopupMenu(context, viewHolder.imageButton);
                if (Utils.checkIfAdded(cardItem)) {
                    popupMenu.inflate(R.menu.home_card_menu2);
                } else {
                    popupMenu.inflate(R.menu.home_card_menu);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.shareFB:
                                System.out.println(type + " id: " + id);
                                System.out.println("share on fb");
                                Utils.openFacebook(context, type, id);
                                break;
                            case R.id.shareTT:
                                System.out.println(type + " id: " + id);
                                System.out.println("share on TT");
                                Utils.openTwitter(context, type, id);
                                break;
                            case R.id.openInTMDB:
                                System.out.println(type + " id: " + id);
                                System.out.println("open in TMDB");
                                openTMDB(type, id);
                                break;
                            case R.id.addToList:
                                System.out.println("adding " + type + " id: " + id);
                                String title = cardItem.getTitle();
                                Utils.addToList(cardItem);
                                break;
                            case R.id.removeFromList:
                                Utils.removeFromList(cardItem);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
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
