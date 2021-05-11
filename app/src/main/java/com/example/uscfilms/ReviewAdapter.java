package com.example.uscfilms;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private ArrayList<ReviewData> localDataSet;
    Context context;
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView by;
        private final TextView rating;
        private final TextView review;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            by = (TextView) view.findViewById(R.id.by);
            rating = (TextView) view.findViewById(R.id.rating);
            review = (TextView) view.findViewById(R.id.review);
        }

        public TextView getBy() {
            return by;
        }

        public TextView getRating() {
            return rating;
        }

        public TextView getReview() {
            return review;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public ReviewAdapter(ArrayList<ReviewData> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.review_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        ReviewData reviewItem = localDataSet.get(position);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        viewHolder.by.setText(reviewItem.getBy());
        viewHolder.review.setText(reviewItem.getContent());
        viewHolder.rating.setText(reviewItem.getRating());
        // onClick
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openReviewActivity(context, reviewItem.getBy(), reviewItem.getRating(), reviewItem.getContent());
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
