package com.example.cs361_project;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String TABLENAME ="MealDetail";
    public DBHelper(Context context) {
        super(context, "Mealdata.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table "+TABLENAME+"(id integer primary key,foodname text,calories text)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String query = "drop table if exists "+TABLENAME+"";
        db.execSQL(query);
        onCreate(db);
    }

    public String sumCal(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String sumCal;
        String sQuery = "select sum(calories) from "+ TABLENAME;
        Cursor cursor = sqLiteDatabase.rawQuery(sQuery, null);

        if(cursor.moveToFirst()){
            sumCal = String.valueOf(cursor.getInt(0));
        }else {
            sumCal = "0";
        }
        cursor.close();
        sqLiteDatabase.close();

        return sumCal;

    }


}
