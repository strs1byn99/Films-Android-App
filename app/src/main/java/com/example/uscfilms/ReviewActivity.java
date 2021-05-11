package com.example.uscfilms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ReviewActivity extends AppCompatActivity {

    String by;
    String rating;
    String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        // getIntent() is a method from the started activity
        Intent myIntent = getIntent();
        by = myIntent.getStringExtra("by");
        rating = myIntent.getStringExtra("rating");
        content = myIntent.getStringExtra("content");
        setContent();
    }

    private void setContent() {
        TextView byView = findViewById(R.id.by);
        TextView ratingView = findViewById(R.id.rating);
        TextView contentView = findViewById(R.id.review_content);
        byView.setText(by);
        ratingView.setText(rating);
        contentView.setText(content);
    }
}