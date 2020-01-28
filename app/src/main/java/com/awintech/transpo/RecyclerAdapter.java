package com.awintech.transpo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.BusViewHolder> {

    public Context context ;
    List<Dashboard_Company_Data> Dashboard_Data;

    public RecyclerAdapter(Context context, List<Dashboard_Company_Data> Dashboard_Data) {
        this.context = context;
        this.Dashboard_Data = Dashboard_Data;
    }

    public String names [] = {"STC Bus", "Vip","STC Bus", "Vip","STC Bus", "Vip","STC Bus", "Vip"};
    public String names1 [] = {"Ghana's no. One transportation Company", "Then Best for the Best patrons","Ghana's no. One transportation Company", "Then Best for the Best patrons",
            "Ghana's no. One transportation Company", "Then Best for the Best patrons","Ghana's no. One transportation Company", "Then Best for the Best patrons"};
    public int images [] = {R.drawable.imageone, R.drawable.abstract_0005,R.drawable.imageone, R.drawable.abstract_0005,R.drawable.imageone, R.drawable.abstract_0005,
            R.drawable.imageone, R.drawable.abstract_0005};
    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.card_item, viewGroup, false);

      final   BusViewHolder busViewHolder = new BusViewHolder(v);
        busViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), All_LocationActivity.class);
                intent.putExtra("COMPANY_NAME",Dashboard_Data.get(busViewHolder.getAdapterPosition()).getCompany_name());
                intent.putExtra("COMPANY_ID",Dashboard_Data.get(busViewHolder.getAdapterPosition()).getCompany_Id());
                v.getContext().startActivity(intent);
            }
        });

        return busViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder busViewHolder, int i) {


        busViewHolder.cardName.setText(Dashboard_Data.get(i).Company_name);
        busViewHolder.cardName1.setText(Dashboard_Data.get(i).Company_message);
        Glide.with(busViewHolder.itemView.getContext()).load(Dashboard_Data.get(i).Company_Image).into(busViewHolder.cardImage);
 }

    @Override
    public int getItemCount() {
        return Dashboard_Data.size();
    }


    class BusViewHolder extends RecyclerView.ViewHolder{
        public TextView cardName;
        public TextView cardName1;
        public ImageView cardImage;
        public BusViewHolder(@NonNull View itemView) {
            super(itemView);
            cardName = itemView.findViewById(R.id.cardName);
            cardName1 = itemView.findViewById(R.id.cardName1);
            cardImage = itemView.findViewById(R.id.cardImage);
        }
    }
}