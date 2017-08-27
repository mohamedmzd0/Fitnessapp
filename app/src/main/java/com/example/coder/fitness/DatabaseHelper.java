package com.example.coder.fitness;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    final String workout = "workout";
    final String progress = "progress";

    public DatabaseHelper(Context context) {
        super(context, "data", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + workout + "( name VARCHAR(30) PRIMARY KEY , desc VARCHAR(100) , played BOOLEAN , url VARCHAR(100) ) ");
        db.execSQL("CREATE TABLE " + progress + "( name VARCHAR(30) PRIMARY KEY ,date VARCHAR(30) , status VARCHAR(30) ) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + workout);
        db.execSQL("DROP TABLE IF EXISTS " + progress);
    }

    public void insertWorkout(String name, String desc, boolean played, String url) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("desc", desc);
        cv.put("played", played);
        cv.put("url", url);
        database.insert(workout, null, cv);
    }

    public void insertprogress(String name, String date, String status) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("date", date);
        cv.put("status", status);
        database.insert(progress, null, cv);
    }

    public ArrayList restoreworkouts() {
        ArrayList<FitnessModel> fitnessModels = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + workout, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            fitnessModels.add(new FitnessModel(cursor.getString(cursor.getColumnIndex("desc")),
                    cursor.getString(cursor.getColumnIndex("name")),
                    (cursor.getInt(cursor.getColumnIndex("played")) == 1),
                    cursor.getString(cursor.getColumnIndex("url"))));
            cursor.moveToNext();
        }
        cursor.close();
        return fitnessModels;
    }

    public ArrayList restorprogress() {
        ArrayList<ProgressModel> progressModels = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + progress, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            progressModels.add(new ProgressModel(cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("date")),
                    cursor.getString(cursor.getColumnIndex("status"))));
            cursor.moveToNext();
        }
        cursor.close();
        return progressModels;
    }
}
