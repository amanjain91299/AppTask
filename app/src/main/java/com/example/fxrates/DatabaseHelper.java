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
      //String sql = "create table fxrate(base varchar(10),date varchar(20),rates varchar(5000))";
      String sql = "create table fxrate(base varchar(10),date varchar(20),GBP varchar(10),HKD varchar(10),IDR varchar(10),ILS varchar(10),DKK varchar(10),INR varchar(10),CHF varchar(10),MXN varchar(10),CZK varchar(10),SGD varchar(10),THB varchar(10),HRK varchar(10),MYR varchar(10),NOK varchar(10),CNY varchar(10),BGN varchar(10),PHP varchar(10),SEK varchar(10),PLN varchar(10),ZAR varchar(10),CAD varchar(10),ISK varchar(10),BRL varchar(10),RON varchar(10),NZD varchar(10),_TRY varchar(10),JPY varchar(10),RUB varchar(10),KRW varchar(10),USD varchar(10),HUF varchar(10),AUD varchar(10))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
