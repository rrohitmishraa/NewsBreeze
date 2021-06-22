package com.rohit.newsbreeze.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rohit.newsbreeze.Adapter.NewsAdapter;
import com.rohit.newsbreeze.Helper.NewsDataModel;
import com.rohit.newsbreeze.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    ImageView mBtnSaved;
    RecyclerView mRecyclerView;
    NewsAdapter adapter;
    ArrayList<NewsDataModel> data;
    EditText mSearch;
    LottieAnimationView mLoadingAnimation;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
    }

    public void init() {
        data = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        mRecyclerView = findViewById(R.id.xRecyclerView);
        mSearch = findViewById(R.id.xSearch);
        mLoadingAnimation = findViewById(R.id.xLoadingAnimation);
        mBtnSaved = findViewById(R.id.xBtnSaved);

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

        mBtnSaved.setOnClickListener(v -> {
            Intent i = new Intent(this, SavedActivity.class);
            startActivity(i);
        });

        fetchFromApi();
    }

    private void filter(String text) {
        ArrayList<NewsDataModel> filteredList = new ArrayList<>();

        for(NewsDataModel item : data) {
            if(item.getHeadLine().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
    }

    private void fetchFromApi() {
        String url = getString(R.string.api_url) + getString(R.string.api_key);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("articles");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject articles = jsonArray.getJSONObject(i);

                            String heading = articles.getString("title");
                            String description = articles.getString("description");
                            String image = articles.getString("urlToImage");
                            String author = articles.getString("author");
                            String content = articles.getString("content");
                            String date = articles.getString("publishedAt").substring(0, 10);

                            JSONObject sourceObject = articles.getJSONObject("source");
                            String source = sourceObject.getString("name");

                            NewsDataModel newsDataModel = new NewsDataModel(image, heading, description, date, author, source, content, i);
                            data.add(newsDataModel);

                            adapter = new NewsAdapter(HomeActivity.this, data, "home");
                            mRecyclerView.setAdapter(adapter);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            mLoadingAnimation.setVisibility(View.INVISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Toast.makeText(this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("User-Agent", "Mozilla/5.0");
                return headers;
            }
        };

        requestQueue.add(request);
    }
}