package com.example.uscfilms;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    //    private String[] localDataSet;
    private ArrayList<SearchData> localDataSet;
    Context context;
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView typeText;
        private final TextView titleText;
        private final TextView ratingText;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            imageView = (ImageView) view.findViewById(R.id.searchImage);
            typeText = (TextView) view.findViewById(R.id.search_type);
            titleText = (TextView) view.findViewById(R.id.search_title);
            ratingText = (TextView) view.findViewById(R.id.search_rating);
        }

        public ImageView getImageView() {
            return imageView;
        }

        public TextView getTypeText() {
            return typeText;
        }

        public TextView getTitleText() {
            return titleText;
        }

        public TextView getRatingText() {
            return ratingText;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public SearchAdapter(ArrayList<SearchData> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        SearchData searchItem = localDataSet.get(position);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        Glide.with(viewHolder.itemView)
                .load(searchItem.getImgUrl())
                .fitCenter()
                .into(viewHolder.imageView);

        viewHolder.ratingText.setText(searchItem.getRating());
        viewHolder.titleText.setText(searchItem.getTitle());
        viewHolder.typeText.setText(searchItem.getType() + "(" + searchItem.getYear() + ")");

        // onClick
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = searchItem.getId();
                System.out.println("clicks on image " + id);
                // get data and open detail page
                Utils.openDetailActivity(context, searchItem.getType(), searchItem.getId().toString());
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
