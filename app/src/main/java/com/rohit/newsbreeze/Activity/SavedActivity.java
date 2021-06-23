package com.rohit.newsbreeze.Activity;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.rohit.newsbreeze.Adapter.NewsAdapter;
import com.rohit.newsbreeze.Helper.DBHelper;
import com.rohit.newsbreeze.Helper.NewsDataModel;
import com.rohit.newsbreeze.R;

import java.util.ArrayList;

public class SavedActivity extends AppCompatActivity {

    ImageView mBtnBack;
    RecyclerView mRecyclerView;
    NewsAdapter adapter;
    ArrayList<NewsDataModel> data;
    EditText mSearch;
    LottieAnimationView mLoadingAnimation;
    TextView mEmptyIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        init();

        mBtnBack.setOnClickListener(v -> {
            this.onBackPressed();
        });
    }

    public void init() {
        data = new ArrayList<>();

        mBtnBack = findViewById(R.id.xBtnBack);
        mRecyclerView = findViewById(R.id.xRecyclerView);
        mSearch = findViewById(R.id.xSearch);
        mLoadingAnimation = findViewById(R.id.xLoadingAnimation);
        mEmptyIndicator = findViewById(R.id.xEmptyIndicator);

        mEmptyIndicator.setVisibility(View.GONE);

        mLoadingAnimation.setVisibility(View.VISIBLE);

        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        fetchFromDatabase();
    }

    private void filter(String text) {
        ArrayList<NewsDataModel> filteredList = new ArrayList<>();

        for (NewsDataModel item : data) {
            if (item.getHeadLine().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
    }

    private void fetchFromDatabase() {
        Cursor cursor = new DBHelper(SavedActivity.this).getCompleteData();

        if(cursor.getCount() > 0) {
            while ((cursor.moveToNext())) {
                NewsDataModel newsDataModel = new NewsDataModel(
                        cursor.getString(2),
                        cursor.getString(1),
                        cursor.getString(4),
                        cursor.getString(7),
                        cursor.getString(6),
                        cursor.getString(5),
                        cursor.getString(8),
                        cursor.getInt(0));

                data.add(newsDataModel);

                adapter = new NewsAdapter(SavedActivity.this, data, "saved");
                mRecyclerView.setAdapter(adapter);
                mRecyclerView.setVisibility(View.VISIBLE);
                mEmptyIndicator.setVisibility(View.GONE);
                mLoadingAnimation.setVisibility(View.INVISIBLE);
            }
        } else {
            mLoadingAnimation.setAnimation("empty.json");
            mEmptyIndicator.setVisibility(View.VISIBLE);
        }
    }
}