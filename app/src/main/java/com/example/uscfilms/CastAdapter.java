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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {

    private ArrayList<CastData> localDataSet;
    Context context;
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView castName;
        private final LinearLayout linearLayout;
//        private final RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            imageView = (ImageView) view.findViewById(R.id.castImage);
            castName = (TextView) view.findViewById(R.id.castName);
            linearLayout = (LinearLayout) view.findViewById(R.id.cast_item);
//            relativeLayout = (RelativeLayout) view.findViewById(R.id.cast_item);
        }

        public ImageView getImageView() {
            return imageView;
        }
        public TextView getCastName() { return castName; }
        public LinearLayout getLinearLayout() { return linearLayout; }
//        public RelativeLayout getRelativeLayout() { return relativeLayout; }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public CastAdapter(ArrayList<CastData> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.cast_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        CastData castItem = localDataSet.get(position);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        Glide.with(viewHolder.itemView)
                .load(castItem.getImgUrl())
                .fitCenter()
                .into(viewHolder.imageView);

        int screenWidth = getScreenWidth();
        int imageWidth = (int) (screenWidth * 0.28);
        int paddingLR = (int) (screenWidth * 0.16 / 6);
        int paddingTB = 20;

        // left, top, right, bottom
        viewHolder.linearLayout.setPadding(paddingLR, paddingTB, paddingLR, paddingTB);
//        viewHolder.relativeLayout.setPadding(paddingLR, paddingTB, paddingLR, paddingTB);

        LinearLayout.LayoutParams ImageLayoutParams = new LinearLayout.LayoutParams(imageWidth, imageWidth);
        viewHolder.imageView.setLayoutParams(ImageLayoutParams);


        LinearLayout.LayoutParams TextLayoutParams = new LinearLayout.LayoutParams(imageWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        viewHolder.castName.setText(castItem.getName());
        viewHolder.castName.setLayoutParams(TextLayoutParams);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
}
