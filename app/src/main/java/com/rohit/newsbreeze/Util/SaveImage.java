package com.rohit.newsbreeze.Util;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

import static android.content.Context.MODE_PRIVATE;

public class SaveImage {

    public String save(Context ctx, ImageView imageView) {

        Bitmap bmp = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        ContextWrapper wrapper = new ContextWrapper(ctx);
        File file = wrapper.getDir("Images",MODE_PRIVATE);
        File path = new File(file, System.currentTimeMillis()+".jpg");

        try {
            FileOutputStream fos = new FileOutputStream(path);

            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            Toast.makeText(ctx, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return path+"";
    }
}
