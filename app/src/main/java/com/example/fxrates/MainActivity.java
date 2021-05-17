package com.example.fxrates;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.fxrates.adapter.RateAdapter;
import com.example.fxrates.dao.FxRatesDAO;
import com.example.fxrates.databinding.ActivityMainBinding;
import com.example.fxrates.modal.Data;
import com.example.fxrates.modal.FxRates;
import com.example.fxrates.modal.Output;
import com.example.fxrates.modal.Rates;
import com.google.gson.JsonObject;

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
        getData();
        binding.btnRefersh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FxRates fxRates = FxRatesDAO.getRates(MainActivity.this);
                showUpdatedData(fxRates);
                Log.e("Rates:==>",fxRates.getRates());
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
        Log.e("Rates","==>"+rates.toString());
        FxRates fxRates = new FxRates(data.getBase(),data.getDate(),rates.toString());
        FxRatesDAO.saveRates(fxRates,MainActivity.this);
        try{
            al.clear();
            JSONObject obj = new JSONObject(rates.toString());
            Iterator<String> itr = obj.keys();
            while(itr.hasNext()){
                String key = itr.next();
                String value = obj.get(key).toString();
                al.add(new Output(key,value));
            }
            adapter = new RateAdapter(MainActivity.this,al);
            binding.rv.setAdapter(adapter);
            binding.rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            adapter.setOnClickListener(new RateAdapter.OnRecyclerViewClick() {
                @Override
                public void onItemClick(Output output, int positioin) {
                  FxRatesDAO.deleteRates(output.getKey(),data,MainActivity.this,fxRates.getRates());
                      al.remove(positioin);
                      adapter.notifyDataSetChanged();
                      Toast.makeText(MainActivity.this, "Data removed..", Toast.LENGTH_SHORT).show();

                }
            });
        }
        catch (Exception e){
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
            Log.e("Error","====>"+e);
        }
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
    private void showUpdatedData(FxRates fxRates){
        try{
            JSONObject obj = new JSONObject(fxRates.getRates());
            Iterator<String> itr = obj.keys();
            al.clear();
            while(itr.hasNext()){
                String key = itr.next();
                String value = obj.get(key).toString();
                al.add(new Output(key,value));
            }
            adapter = new RateAdapter(MainActivity.this,al);
            binding.rv.setAdapter(adapter);
            binding.rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            adapter.setOnClickListener(new RateAdapter.OnRecyclerViewClick() {
                @Override
                public void onItemClick(Output output, int positioin) {
                        FxRatesDAO.deleteRates(output.getKey(),data,MainActivity.this,fxRates.getRates());
                        al.remove(positioin);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Data removed..", Toast.LENGTH_SHORT).show();

                }
            });
        }
        catch (Exception e){
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
            Log.e("Error:::","====>"+e);
        }
    }
}