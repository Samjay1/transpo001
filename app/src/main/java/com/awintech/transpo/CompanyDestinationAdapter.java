package com.awintech.transpo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.DialogTitle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CompanyDestinationAdapter extends RecyclerView.Adapter<CompanyDestinationAdapter.CompanyDestinatinViewHolder> {

    public int id;

    List<Company_Destination_Data> company_destination_data;
    Context context;

    public CompanyDestinationAdapter (Context context, List<Company_Destination_Data> company_destination_data){
        this.context = context;
        this.company_destination_data = company_destination_data;
    }

    Dialog dialog;
    @NonNull
    @Override
    public CompanyDestinatinViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.company_destination_list, viewGroup, false);
       CompanyDestinatinViewHolder companyDestinatinViewHolder = new CompanyDestinatinViewHolder(v);
        return companyDestinatinViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CompanyDestinatinViewHolder companyDestinatinViewHolder, final int i) {
        id = i;

            companyDestinatinViewHolder.date.setText(company_destination_data.get(i).getDate());
            companyDestinatinViewHolder.time.setText(company_destination_data.get(i).getTime());
            companyDestinatinViewHolder.seatleft.setText(company_destination_data.get(i).getSeat_No());
            companyDestinatinViewHolder.duration.setText(company_destination_data.get(i).getDuration());
            companyDestinatinViewHolder.cost.setText(company_destination_data.get(i).getCost());
        Glide.with(companyDestinatinViewHolder.itemView.getContext()).load(company_destination_data.get(i).getCompany_Logo()).into(companyDestinatinViewHolder.companyLogo);

        companyDestinatinViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(v.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.company_destination_confirmtickets_dialog);
                dialog.setCanceledOnTouchOutside(true);

                Button okbtn = (Button) dialog.findViewById(R.id.buybtn);
                TextView companyName = (TextView) dialog.findViewById(R.id.companyName);
                TextView from = (TextView) dialog.findViewById(R.id.from);
                TextView halffrom = (TextView) dialog.findViewById(R.id.halffrom);
                TextView to = (TextView) dialog.findViewById(R.id.to);
                TextView halfto = (TextView) dialog.findViewById(R.id.halfto);
                TextView duration = (TextView) dialog.findViewById(R.id.duration);
                TextView cost = (TextView) dialog.findViewById(R.id.cost);
                TextView seat = (TextView) dialog.findViewById(R.id.seat);
                TextView date = (TextView) dialog.findViewById(R.id.date);
                TextView time = (TextView) dialog.findViewById(R.id.time);
                TextView location = (TextView) dialog.findViewById(R.id.location);
                TextView busNo = (TextView) dialog.findViewById(R.id.busNo);

                //  SETTING THE VALUES OF THE PREVIEW DIALOG BOX
                companyName.setText(company_destination_data.get(i).getCompany_Name());
                from.setText(company_destination_data.get(i).getFrom());
                halffrom.setText(company_destination_data.get(i).getFrom().substring(0,3).trim().toUpperCase());
                to.setText(company_destination_data.get(i).getTo());
                halfto.setText(company_destination_data.get(i).getTo().substring(0,3).trim().toUpperCase());
                duration.setText(company_destination_data.get(i).getDuration());
                cost.setText(company_destination_data.get(i).getCost());
                seat.setText(company_destination_data.get(i).getSeat_No());
                date.setText(company_destination_data.get(i).getDate());
                time.setText(company_destination_data.get(i).getTime());
                location.setText(company_destination_data.get(i).getDepart_Locaton());
                busNo.setText(company_destination_data.get(i).getBus_No());

                okbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), PaymentActivity.class);
                        intent.putExtra("COMPANY_NAME",company_destination_data.get(i).getCompany_Name());
                        intent.putExtra("COMPANY_ID",company_destination_data.get(i).getCompany_Id());
                        intent.putExtra("FROM",company_destination_data.get(i).getFrom());
                        intent.putExtra("TO",company_destination_data.get(i).getTo());
                        intent.putExtra("DURATION",company_destination_data.get(i).getDuration());
                        intent.putExtra("COST",company_destination_data.get(i).getCost());
                        intent.putExtra("SEAT",company_destination_data.get(i).getSeat_No());
                        intent.putExtra("DATE",company_destination_data.get(i).getDate());
                        intent.putExtra("TIME",company_destination_data.get(i).getTime());
                        intent.putExtra("LOCATION",company_destination_data.get(i).getDepart_Locaton());
                        intent.putExtra("BUS",company_destination_data.get(i).getBus_No());
                       v.getContext().startActivity(intent);
                       dialog.cancel();
                    }
                });

                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return company_destination_data.size();
    }

    class CompanyDestinatinViewHolder extends RecyclerView.ViewHolder{

        public TextView date,time,cost,duration,seatleft;
        ImageView companyLogo;

        //Dialog Views
//        Button okbtn ;
//        TextView DcompanyName, Dfrom, Dhalffrom, Dto , Dhalfto , Dduration, Dcost, Dseat, Ddate, Dtime ;

        public CompanyDestinatinViewHolder(@NonNull final View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            cost = itemView.findViewById(R.id.cost);
            time = itemView.findViewById(R.id.time);
            duration = itemView.findViewById(R.id.duration);
            seatleft = itemView.findViewById(R.id.seatleft);
            companyLogo = itemView.findViewById(R.id.companyLogo);


        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
        }
}
