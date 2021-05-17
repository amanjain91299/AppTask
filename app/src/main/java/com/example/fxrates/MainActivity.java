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
                for(Output o : al){
                    if(o.getKey().equals("GBP")){

                    }
                    else if(o.getKey().equals("HKD")){

                    }
                    else if(o.getKey().equals("IDR")){

                    }
                    else if(o.getKey().equals("ILS")){

                    }
                    else if(o.getKey().equals("DKK")){

                    }
                    else if(o.getKey().equals("INR")){

                    }
                    else if(o.getKey().equals("CHF")){

                    }
                    else if(o.getKey().equals("MXN")){

                    }
                    else if(o.getKey().equals("CZK")){

                    }
                    else if(o.getKey().equals("SGD")){

                    }
                    else if(o.getKey().equals("THB")){

                    }
                    else if(o.getKey().equals("HRK")){

                    }
                    else if(o.getKey().equals("MYR")){

                    }
                    else if(o.getKey().equals("NOK")){

                    }
                    else if(o.getKey().equals("CNY")){

                    }
                    else if(o.getKey().equals("BGN")){

                    }
                    else if(o.getKey().equals("PHP")){

                    }
                    else if(o.getKey().equals("SEK")){

                    }
                    else if(o.getKey().equals("PLN")){

                    }
                    else if(o.getKey().equals("ZAR")){

                    }
                    else if(o.getKey().equals("CAD")){

                    }
                    else if(o.getKey().equals("ISK")){

                    }
                    else if(o.getKey().equals("BRL")){

                    }
                    else if(o.getKey().equals("RON")){

                    }
                    else if(o.getKey().equals("NZD")){

                    }
                    else if(o.getKey().equals("TRY")){

                    }
                    else if(o.getKey().equals("JPY")){

                    }
                    else if(o.getKey().equals("RUB")){

                    }
                    else if(o.getKey().equals("KRW")){

                    }
                    else if(o.getKey().equals("USD")){

                    }
                    else if(o.getKey().equals("HUF")){

                    }
                    else if(o.getKey().equals("AUD")){

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