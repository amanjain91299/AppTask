package com.example.fxrates.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fxrates.databinding.RateItemListBinding;
import com.example.fxrates.modal.Output;

import java.util.ArrayList;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.RateViewHolder> {
  private Context context;
  private ArrayList<Output>al;
  OnRecyclerViewClick listener;
  public RateAdapter(Context context,ArrayList<Output>al){
      this.context = context;
      this.al = al;
  }
    @NonNull
    @Override
    public RateViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        RateItemListBinding binding = RateItemListBinding.inflate(LayoutInflater.from(context),parent,false);
        return new RateViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull  RateAdapter.RateViewHolder holder, int position) {
        Output data = al.get(position);
        holder.binding.tvRates.setText(data.getKey());
        holder.binding.tvValue.setText(data.getValue());
   }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public class RateViewHolder extends RecyclerView.ViewHolder{
      RateItemListBinding binding;
      public RateViewHolder(RateItemListBinding binding){
          super(binding.getRoot());
          this.binding = binding;
          this.binding.btnRemove.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  int position = getAdapterPosition();
                  if(position!=RecyclerView.NO_POSITION && listener!=null){
                      Output obj = al.get(position);
                      listener.onItemClick(obj,position);
                  }
              }
          });
      }
  }
  public interface OnRecyclerViewClick{
      void onItemClick(Output output,int positioin);
  }
  public void setOnClickListener(OnRecyclerViewClick listener){
      this.listener = listener;
  }
}
