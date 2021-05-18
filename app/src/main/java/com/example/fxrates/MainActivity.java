package com.example.fxrates;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.fxrates.adapter.RateAdapter;
import com.example.fxrates.databinding.ActivityMainBinding;
import com.example.fxrates.modal.Data;
import com.example.fxrates.modal.Output;
import com.example.fxrates.modal.Rates;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Data data;
    ArrayList<Output>al;
    RateAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        al = new ArrayList<>();
        adapter = new RateAdapter(this,al);
        getData();
        binding.btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromValue = binding.etConvert.getText().toString();
                double value = Double.parseDouble(fromValue);
                double result = 0;
                for(Output o : al){
                    if(!o.getValue().equals("")) {
                        if (o.getKey().equals("GBP")) {
                            result = value / 1.15;
                        } else if (o.getKey().equals("HKD")) {
                            result = value * 9.44806;
                        } else if (o.getKey().equals("IDR")) {
                            result = value * 17377.1;
                        } else if (o.getKey().equals("ILS")) {
                            result = value * 3.7;
                        } else if (o.getKey().equals("DKK")) {
                            result = value * 7.44;
                        } else if (o.getKey().equals("INR")) {
                            result = value * 88.807;
                        } else if (o.getKey().equals("CHF")) {
                            result = value * 1.0952;
                        } else if (o.getKey().equals("MXN")) {
                            result = value * 24.0232;
                        } else if (o.getKey().equals("CZK")) {
                            result = value * 25.5492;
                        } else if (o.getKey().equals("SGD")) {
                            result = value * 1.6161;
                        } else if (o.getKey().equals("THB")) {
                            result = value * 38.012;
                        } else if (o.getKey().equals("HRK")) {
                            result = value * 7.519;
                        } else if (o.getKey().equals("MYR")) {
                            result = value * 5.0013;
                        } else if (o.getKey().equals("NOK")) {
                            result = value * 10.0074;
                        } else if (o.getKey().equals("CNY")) {
                            result = value * 7.8024;
                        } else if (o.getKey().equals("BGN")) {
                            result = value * 1.9558;
                        } else if (o.getKey().equals("PHP")) {
                            result = value * 57.867;
                        } else if (o.getKey().equals("SEK")) {
                            result = value * 10.1265;
                        } else if (o.getKey().equals("PLN")) {
                            result = value * 4.5218;
                        } else if (o.getKey().equals("ZAR")) {
                            result = value * 17.0722;
                        } else if (o.getKey().equals("CAD")) {
                            result = value * 1.47;
                        } else if (o.getKey().equals("ISK")) {
                            result = value * 150.9;
                        } else if (o.getKey().equals("BRL")) {
                            result = value * 6.3942;
                        } else if (o.getKey().equals("RON")) {
                            result = value * 4.9259;
                        } else if (o.getKey().equals("NZD")) {
                            result = value * 1.6771;
                        } else if (o.getKey().equals("TRY")) {
                            result = value * 10.2174;
                        } else if (o.getKey().equals("JPY")) {
                            result = value * 132.49;
                        } else if (o.getKey().equals("RUB")) {
                            result = value * 89.6216;
                        } else if (o.getKey().equals("KRW")) {
                            result = value * 1368.07;
                        } else if (o.getKey().equals("USD")) {
                            result = value * 1.2123;
                        } else if (o.getKey().equals("HUF")) {
                            result = value * 355.52;
                        } else if (o.getKey().equals("AUD")) {
                            result = value * 1.5634;
                        }
                        DatabaseHelper helper = new DatabaseHelper(MainActivity.this);
                        SQLiteDatabase database = helper.getReadableDatabase();
                        ContentValues values = new ContentValues();
                        values.put(o.getKey(),""+result);
                        database.update("fxrate",values,null,null);
                        database.close();
                    }
                }
            }
        });
        binding.btnRefersh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper helper = new DatabaseHelper(MainActivity.this);
                SQLiteDatabase database = helper.getReadableDatabase();
                Cursor c = database.rawQuery("select * from fxrate",null);
                int i=2;
                al.clear();
                adapter.notifyDataSetChanged();
                while(c.moveToNext()){
                    while(i<=c.getColumnCount()-2){

                        String key = c.getColumnName(i);
                        String value = c.getString(i);
                        if(!value.equals("")) {
                            Output output = new Output(key, value);
                            al.add(output);
                        }
                        i++;
                    }
                }
                adapter.notifyDataSetChanged();
                database.close();
            }
        });

    }
    public void getData(){
        if(isInternetConnected()){
            Retrofit retrofit = RetrofitClient.getRetrofitInstance();
            retrofit.create(FxRatesApi.class).getData().enqueue(new Callback<Data>() {
                @Override
                public void onResponse(Call<Data> call, Response<Data> response) {
                    if(response.code()==200){
                        Toast.makeText(MainActivity.this, ""+response.body(), Toast.LENGTH_SHORT).show();
                        data = response.body();
                        ActionBar ab = getSupportActionBar();
                        ab.setTitle(data.getBase());
                        ab.setSubtitle(data.getDate());
                        populateDataOnUi(data);
                    }
                    else
                        Toast.makeText(MainActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Data> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    Log.e("Error","==>"+t);
                }
            });
        }
        else{
            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
            ab.setTitle("Alert !");
            ab.setMessage("Please enable internet connection then restart app.");
            ab.setPositiveButton("OK",null);
            ab.show();
        }
    }

    private void populateDataOnUi(Data data){
        Rates rates = data.getRates();
        try{
            al.clear();
            adapter.notifyDataSetChanged();
            JSONObject obj = new JSONObject(rates.toString());
            Iterator<String> itr = obj.keys();

            while(itr.hasNext()){
                String key = itr.next();
                String value = obj.get(key).toString();
                al.add(new Output(key.toUpperCase(),value));
            }
            saveDataIntoSqlite(al,data);
            adapter = new RateAdapter(MainActivity.this,al);
            binding.rv.setAdapter(adapter);
            binding.rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            adapter.setOnClickListener(new RateAdapter.OnRecyclerViewClick() {
                @Override
                public void onItemClick(Output output, int positioin) {
                     DatabaseHelper helper = new DatabaseHelper(MainActivity.this);
                     SQLiteDatabase database = helper.getWritableDatabase();
                     ContentValues values = new ContentValues();
                     values.put(output.getKey().toUpperCase(),"");
                     database.update("fxrate",values,null,null);
                     Toast.makeText(MainActivity.this, "Record Deleted", Toast.LENGTH_SHORT).show();
                     al.remove(positioin);
                     adapter.notifyDataSetChanged();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
            Log.e("Error","====>"+e);
        }
    }
    private void saveDataIntoSqlite(ArrayList<Output> al,Data data){

        ContentValues values = new ContentValues();
        for(Output output : al){
            values.put(output.getKey(),output.getValue());
        }
        values.put("base",data.getBase());
        values.put("date",data.getDate());
        DatabaseHelper helper  = new DatabaseHelper(MainActivity.this);

        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("fxrate",null,null);
        db.insert("fxrate",null,values);
        db.close();
    }
    public boolean isInternetConnected(){
        boolean networkStatus = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            networkStatus = true;
        }
        else
            networkStatus = false;
        return networkStatus;
    }
}