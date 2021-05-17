package com.example.fxrates;

import com.example.fxrates.modal.Data;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FxRatesApi {
  @GET("latest")
  public Call<Data> getData();
}
