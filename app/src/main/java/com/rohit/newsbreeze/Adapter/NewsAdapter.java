package com.rohit.newsbreeze.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.rohit.newsbreeze.Activity.NewsDetailsActivity;
import com.rohit.newsbreeze.Helper.DBHelper;
import com.rohit.newsbreeze.Helper.NewsDataModel;
import com.rohit.newsbreeze.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private final Context ctx;
    private final String comingFrom;
    private ArrayList<NewsDataModel> data;

    public NewsAdapter(Context ctx, ArrayList<NewsDataModel> data, String comingFrom) {
        this.ctx = ctx;
        this.data = data;
        this.comingFrom = comingFrom;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        LayoutInflater lf = LayoutInflater.from(ctx);

        if (comingFrom.equalsIgnoreCase("home")) {
            v = lf.inflate(R.layout.home_list_item, parent, false);
        } else {
            v = lf.inflate(R.layout.saved_list_item, parent, false);
        }

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.ViewHolder holder, int position) {
        holder.mHeadLine.setText(data.get(position).getHeadLine());

        if (comingFrom.equalsIgnoreCase("home")) {
            String des = data.get(position).getDescription();
            if (des.isEmpty() || des.equalsIgnoreCase("null")) {
                holder.mDescription.setVisibility(View.GONE);
            } else {
                holder.mDescription.setText(des);
            }

            holder.mDate.setText(data.get(position).getDate());

            DBHelper dbHelper = new DBHelper(ctx);

            if (dbHelper.checkIfUrlExist(data.get(position).getImage())) {
                holder.mBtnSave.setBackground(ctx.getDrawable(R.drawable.bg_green_rounded));
            } else {
                holder.mBtnSave.setBackground(ctx.getDrawable(R.drawable.bg_grey_rounded));
            }

            holder.mBtnSave.setOnClickListener(v -> {
                if (dbHelper.checkIfUrlExist(data.get(position).getImage())) {
                    dbHelper.delete(data.get(position).getImage());

                    holder.mBtnSave.setBackground(ctx.getDrawable(R.drawable.bg_grey_rounded));
                } else {
                    //Save Image

                    ContextWrapper wrapper = new ContextWrapper(ctx);
                    File file = wrapper.getDir("Images",MODE_PRIVATE);
                    File path = new File(file, System.currentTimeMillis()+".jpg");

                    Bitmap bmp = ((BitmapDrawable)holder.mPreviewImage.getDrawable()).getBitmap();
                    
                    try {
                        FileOutputStream fos = new FileOutputStream(path);

                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    } catch (Exception e) {
                        Toast.makeText(ctx, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(ctx, "" + path, Toast.LENGTH_SHORT).show();
                    ///////////////////////////////

                    boolean insertData = dbHelper.addData(data.get(position).getHeadLine(), data.get(position).getImage(), path + "", data.get(position).getDescription()
                            , data.get(position).getSource(), data.get(position).getAuthor(), data.get(position).getAuthor(), data.get(position).getContent());

                    if (insertData) Toast.makeText(ctx, "Saved", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(ctx, "There was some error.", Toast.LENGTH_SHORT).show();


                    holder.mBtnSave.setBackground(ctx.getDrawable(R.drawable.bg_green_rounded));
                }

            });

        } else {
            holder.mDate.setText(data.get(position).getDate() + "\u2022" + data.get(position).getAuthor());
        }

        Picasso.get()
                .load(data.get(position).getImage())
                .placeholder(R.drawable.no_image)
                .into(holder.mPreviewImage);

        holder.mParentLayout.setOnClickListener(v -> {
            Intent i;
            i = new Intent(ctx, NewsDetailsActivity.class);


            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) ctx,
                    holder.mPreviewImage,
                    ViewCompat.getTransitionName(holder.mPreviewImage));
            i.putExtra("position", data.get(position).getPos());
            i.putExtra("comingFrom", comingFrom);
            ctx.startActivity(i, optionsCompat.toBundle());
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void filterList(ArrayList<NewsDataModel> filteredList) {
        data = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mHeadLine, mDescription, mDate;
        ImageView mPreviewImage, mBtnSave;
        RelativeLayout mParentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            mHeadLine = itemView.findViewById(R.id.xHeadline);
            mDescription = itemView.findViewById(R.id.xDescription);
            mDate = itemView.findViewById(R.id.xDate);
            mBtnSave = itemView.findViewById(R.id.xBtnSave);
            mPreviewImage = itemView.findViewById(R.id.xPreviewImage);
            mParentLayout = itemView.findViewById(R.id.xParentLayout);
        }
    }
}
