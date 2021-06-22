package com.rohit.newsbreeze.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class DBHelper extends SQLiteOpenHelper {

    String TABLE_NAME = "saved_news";

    public DBHelper(Context context) {
        super(context, "newsbreeze.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "image_url TEXT," +
                "description TEXT," +
                "source TEXT," +
                "author TEXT," +
                "date TEXT," +
                "content TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }

    public boolean addData(String title, String image_url, String description, String source, String author, String date, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("title", title);
        contentValues.put("image_url", image_url);
        contentValues.put("description", description);
        contentValues.put("source", source);
        contentValues.put("author", author);
        contentValues.put("date", date);
        contentValues.put("content", content);

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }

    public boolean checkIfUrlExist(String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "SELECT * FROM " + TABLE_NAME + " WHERE '" + data + "' = " + "image_url";
        Cursor cursor = db.rawQuery(Query, null);

        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public void delete(String data) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "image_url=?", new String[] {data});
    }

    public Cursor getCompleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC";
        Cursor result = db.rawQuery(query, null);
        return result;
    }

    public Cursor getData(int pos) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE id = " + pos;
        Cursor result = db.rawQuery(query, null);
        return result;
    }
}
