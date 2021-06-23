package com.rohit.newsbreeze.Activity;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rohit.newsbreeze.Helper.DBHelper;
import com.rohit.newsbreeze.R;
import com.rohit.newsbreeze.Util.SaveImage;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class NewsDetailsActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    TextView mHeading, mDate, mAuthorName, mSource, mContent, mDetailBtnSave;
    LottieAnimationView mDetailBtnSaveAnimation;
    ImageView mPreviewImage, mDetailBtnBack;
    DBHelper dbHelper;
    boolean exist;
    String title, image_url, description, source, author, date, content;

    View.OnClickListener clickToSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mDetailBtnSaveAnimation.getFrame() == 56) {
                mDetailBtnSaveAnimation.setSpeed(-4);
                mDetailBtnSaveAnimation.playAnimation();

                mDetailBtnSave.setBackground(getResources().getDrawable(R.drawable.bg_green_rounded));
                mDetailBtnSave.setText("Save");

                dbHelper.delete(image_url);

                Toast.makeText(NewsDetailsActivity.this, "Removed", Toast.LENGTH_SHORT).show();
            } else {
                mDetailBtnSaveAnimation.setSpeed(2);
                mDetailBtnSaveAnimation.playAnimation();

                mDetailBtnSave.setBackground(getResources().getDrawable(R.drawable.bg_grey_rounded));
                mDetailBtnSave.setText("Saved");

                SaveImage saveImage = new SaveImage();
                String path = saveImage.save(NewsDetailsActivity.this, mPreviewImage);

                addDataToDb(title, image_url, path, description, source, author, date, content);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        Fade fade = new Fade();
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        init();
    }

    private void init() {
        mHeading = findViewById(R.id.xDetailHeading);
        mDate = findViewById(R.id.xDetailDate);
        mAuthorName = findViewById(R.id.xDetailAuthorName);
        mSource = findViewById(R.id.xDetailSource);
        mContent = findViewById(R.id.xDetailContent);
        mPreviewImage = findViewById(R.id.xDetailPreviewImage);
        mDetailBtnBack = findViewById(R.id.xDetailBtnBack);
        mDetailBtnSaveAnimation = findViewById(R.id.xDetailBtnSaveAnimation);
        mDetailBtnSave = findViewById(R.id.xDetailBtnSave);

        dbHelper = new DBHelper(this);

        requestQueue = Volley.newRequestQueue(this);

        mDetailBtnBack.setColorFilter(Color.argb(255, 255, 255, 255));
        mDetailBtnBack.setOnClickListener(v -> this.onBackPressed());

        mDetailBtnSaveAnimation.setVisibility(View.GONE);
        mDetailBtnSave.setVisibility(View.GONE);

        mDetailBtnSaveAnimation.setOnClickListener(clickToSave);
        mDetailBtnSave.setOnClickListener(clickToSave);

        int position = getIntent().getIntExtra("position", 0);
        String comingFrom = getIntent().getStringExtra("comingFrom");

        if (comingFrom.equalsIgnoreCase("home")) {
            fetchFromApi(position);
        } else {
            fetchFromDatabase(position);
        }
    }

    private void fetchFromApi(int position) {
        String url = getString(R.string.api_url) + getString(R.string.api_key);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("articles");

                        JSONObject articles = jsonArray.getJSONObject(position);

                        title = articles.getString("title");
                        image_url = articles.getString("urlToImage");
                        description = articles.getString("description");
                        author = articles.getString("author");
                        date = articles.getString("publishedAt").substring(0, 10);
                        content = articles.getString("content");

                        mDate.setText(date);
                        mHeading.setText(title);
                        mContent.setText(content);
                        mAuthorName.setText(author);

                        JSONObject sourceObject = articles.getJSONObject("source");
                        source = sourceObject.getString("name");
                        mSource.setText(source);

                        Picasso.get()
                                .load(image_url)
                                .placeholder(R.drawable.no_image)
                                .into(mPreviewImage);

                        exist = dbHelper.checkIfUrlExist(image_url);

                        if (exist) {
                            mDetailBtnSaveAnimation.setFrame(56);

                            mDetailBtnSave.setBackground(getResources().getDrawable(R.drawable.bg_grey_rounded));
                            mDetailBtnSave.setText("Saved");
                        } else {
                            mDetailBtnSaveAnimation.setFrame(0);

                            mDetailBtnSave.setBackground(getResources().getDrawable(R.drawable.bg_green_rounded));
                            mDetailBtnSave.setText("Save");
                        }

                        mDetailBtnSaveAnimation.setVisibility(View.VISIBLE);
                        mDetailBtnSave.setVisibility(View.VISIBLE);

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

    private void fetchFromDatabase(int position) {
        Cursor cursor = new DBHelper(NewsDetailsActivity.this).getData(position);

        cursor.moveToFirst();

        mDate.setText(cursor.getString(7));
        mHeading.setText(cursor.getString(1));
        mContent.setText(cursor.getString(8));
        mAuthorName.setText(cursor.getString(6));
        mSource.setText(cursor.getString(5));

        File imgFile = new  File(cursor.getString(3));
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            mPreviewImage.setImageBitmap(myBitmap);
        }
    }

    public void addDataToDb(String title, String image_url, String image, String description, String source, String author, String date, String content) {
        boolean insertData = dbHelper.addData(title, image_url, image, description, source, author, date, content);

        if (insertData) Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        else Toast.makeText(this, "There was some error.", Toast.LENGTH_SHORT).show();
    }
}