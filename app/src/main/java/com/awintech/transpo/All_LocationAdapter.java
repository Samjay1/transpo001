package com.awintech.transpo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ConcurrentModificationException;
import java.util.List;

public class All_LocationAdapter extends RecyclerView.Adapter<All_LocationAdapter.All_LocationViewHolder>{

    Context context;
    List<All_Location_Data> all_locaion_data;

    public All_LocationAdapter(Context context, List<All_Location_Data> all_locaion_data) {
        this.context = context;
        this.all_locaion_data = all_locaion_data;
    }


    @NonNull
    @Override
    public All_LocationViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View v = LayoutInflater.from(context).inflate(R.layout.all_location_list, viewGroup,false);
        final All_LocationViewHolder all_locationViewHolder = new All_LocationViewHolder(v);
        all_locationViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Company_destinationsActivity.class);
                intent.putExtra("FROM", all_locaion_data.get(all_locationViewHolder.getAdapterPosition()).getFrom());
                intent.putExtra("TO", all_locaion_data.get(all_locationViewHolder.getAdapterPosition()).getTo());
                intent.putExtra("COMPANY_NAME", all_locaion_data.get(all_locationViewHolder.getAdapterPosition()).getCompany_Name());
                intent.putExtra("COMPANY_ID", all_locaion_data.get(all_locationViewHolder.getAdapterPosition()).getCompany_Id());

                v.getContext().startActivity(intent);
            }
        });

        return all_locationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull All_LocationViewHolder all_locationViewHolder, int i) {

        all_locationViewHolder.from.setText(all_locaion_data.get(i).From);
        all_locationViewHolder.to.setText(all_locaion_data.get(i).To);
        all_locationViewHolder.halffrom.setText(all_locaion_data.get(i).From.substring(0,3).trim().toUpperCase());
        all_locationViewHolder.halfto.setText(all_locaion_data.get(i).To.substring(0,3).trim().toUpperCase());

    }

    @Override
    public int getItemCount() {
        return all_locaion_data.size();
    }

    public class All_LocationViewHolder extends RecyclerView.ViewHolder{

        public TextView halffrom,from,halfto,to;
        public All_LocationViewHolder(@NonNull View itemView) {
            super(itemView);

            halffrom = itemView.findViewById(R.id.halffrom);
            from = itemView.findViewById(R.id.from);
            halfto = itemView.findViewById(R.id.halfto);
            to = itemView.findViewById(R.id.to);

        }
    }
}
