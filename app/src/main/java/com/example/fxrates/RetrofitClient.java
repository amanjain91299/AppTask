package com.example.fxrates;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
  public static Retrofit retrofit;
  public static Retrofit getRetrofitInstance(){
     if(retrofit == null){
         retrofit = new Retrofit.Builder()
                 .baseUrl("https://api.ratesapi.io/api/")
                 .addConverterFactory(GsonConverterFactory.create())
                 .build();
     }
     return retrofit;
  }
}
