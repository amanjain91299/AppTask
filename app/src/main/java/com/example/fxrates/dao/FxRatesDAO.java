package com.example.fxrates.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.fxrates.DatabaseHelper;
import com.example.fxrates.modal.Data;
import com.example.fxrates.modal.FxRates;
import com.google.gson.JsonObject;

import org.json.JSONObject;

public class FxRatesDAO {
  public static FxRates getRates(Context context){
      DatabaseHelper helper = new DatabaseHelper(context);
      SQLiteDatabase db = helper.getReadableDatabase();
      Cursor c = db.rawQuery("select * from fxrate",null);
      FxRates fxRates = null;
      if(c.moveToNext())
      {
         String base =  c.getString(0);
         String date = c.getString(1);
         String rates = c.getString(2);
         fxRates = new FxRates(base,date,rates);
      }
      db.close();
      return fxRates;
  }
  public static void saveRates(FxRates fxRates, Context context){
      DatabaseHelper helper = new DatabaseHelper(context);
      SQLiteDatabase db = helper.getWritableDatabase();
      ContentValues values = new ContentValues();
      values.put("base",fxRates.getBase());
      values.put("date",fxRates.getDate());
      values.put("rates",fxRates.getRates());
      db.insert("fxrate",null,values);
      db.close();
   }
   public static void deleteRates(String key, Data data, Context context,String fxRatesString){
      boolean status = false;
      DatabaseHelper helper = new DatabaseHelper(context);
      SQLiteDatabase db = helper.getWritableDatabase();
      try {
          JSONObject obj = new JSONObject(fxRatesString);
          int keyIndex = fxRatesString.indexOf(key);
          int commaIndex = fxRatesString.indexOf(",",keyIndex);
          StringBuffer sb = new StringBuffer(fxRatesString);
          String updateRateString = sb.replace(keyIndex,commaIndex+1,"").toString();
          String sql = "delete from fxrate";
          db.execSQL(sql);
          sql = "insert into fxrate(base,date,rates) values('"+data.getBase()+"','"+data.getDate()+"','"+updateRateString+"')";
          db.execSQL(sql);
      }
      catch (Exception e){
          Log.e("DARError",""+e);
      }
   }
}
