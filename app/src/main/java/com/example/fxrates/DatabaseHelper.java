package com.example.fxrates;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context context){
        super(context,"fxrate.sqlite",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
      String sql = "create table fxrate(base varchar(10),date varchar(20),rates varchar(5000))";
      db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
