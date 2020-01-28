package com.awintech.transpo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.List;

public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.TicketViewHolder> {
    Context context;
    final List<Ticket_Data> Ticket_data;

    RequestQueue requestQueue;
    public JsonObjectRequest jsonObjectRequest;

    String path = "https://transspo.com/cancelticket/" ;

    public TicketsAdapter (Context context, List<Ticket_Data> Ticket_data){
        this.context = context;
        this.Ticket_data = Ticket_data;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
         context = viewGroup.getContext();

        View v  = LayoutInflater.from(context).inflate(R.layout.ticket_listview_layout, viewGroup, false);
        final TicketViewHolder ticketViewHolder = new TicketViewHolder(v);

        ticketViewHolder.canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trans_id = Ticket_data.get(i).getTransactionID();
                Toast.makeText(context,"From clicked " + trans_id + i, Toast.LENGTH_LONG).show();
                cancelDialog(ticketViewHolder,context, trans_id);
            }
        });



        return ticketViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TicketViewHolder ticketViewHolder, final int i) {

        // Setting the values for the recyclerView
        ticketViewHolder.date.setText(Ticket_data.get(i).getDate());
            ticketViewHolder.from.setText(Ticket_data.get(i).getFrom());
            ticketViewHolder.to.setText("to  "+Ticket_data.get(i).getTo());
            ticketViewHolder.companyName.setText(Ticket_data.get(i).getCompanyName());
//            Toast.makeText(context, Ticket_data.get(i).getCancelState(), Toast.LENGTH_LONG).show();
            ticketViewHolder.canclebtn.setText("Cancel");

            if(Ticket_data.get(i).getCancelState().equals("0")){
                ticketViewHolder.canclebtn.setClickable(false);
                ticketViewHolder.canclebtn.setText("cancelled");
                ticketViewHolder.canclebtn.setBackgroundColor(context.getResources().getColor(R.color.gray));
//                ticketViewHolder.canclebtn.setVisibility(View.INVISIBLE);
            }
            ticketViewHolder.canclebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        cancelDialog(ticketViewHolder, context, Ticket_data.get(i).getTransactionID());

                }
            });

        ticketViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Ticket_PreviewActivity.class);
                intent.putExtra("FROM", Ticket_data.get(i).getFrom());
                intent.putExtra("TO", Ticket_data.get(i).getTo());
                intent.putExtra("COMPANY_NAME", Ticket_data.get(i).getCompanyName());
                intent.putExtra("COMPANY_ID", Ticket_data.get(i).getCompany_id());
                intent.putExtra("SEAT", Ticket_data.get(i).getSeat());
                intent.putExtra("BUS", Ticket_data.get(i).getBus_number());
                intent.putExtra("FIRST_NAME", Ticket_data.get(i).getFirstname());
//                intent.putExtra("SURNAME", Ticket_data.get(i).getSurname());
                intent.putExtra("PHONE", Ticket_data.get(i).getPhone());
                intent.putExtra("LOCATION", Ticket_data.get(i).getLocation());
                intent.putExtra("TIME", Ticket_data.get(i).getTime());
                intent.putExtra("DATE", Ticket_data.get(i).getDate());
                intent.putExtra("AMOUNT", Ticket_data.get(i).getAmount());
                intent.putExtra("TRANSACTION", Ticket_data.get(i).getTransactionID());
//                Toast.makeText(context,"name: " +Ticket_data.get(i).getFirstname(), Toast.LENGTH_LONG).show();

                if(Ticket_data.get(i).getCancelState().equals("1")) {
                    v.getContext().startActivity(intent);
                }
                else {Toast.makeText(context, "Ticket is Cancelled!!!", Toast.LENGTH_SHORT).show();}
            }
        });
    }

    @Override
    public int getItemCount() {
        return Ticket_data.size();
    }

    class TicketViewHolder extends RecyclerView.ViewHolder{
        public TextView location,date,from,to;
        public Button canclebtn;
        public TextView companyName;
        public TicketViewHolder(@NonNull final View itemView) {
            super(itemView);

            companyName = itemView.findViewById(R.id.companyName);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);
            date = itemView.findViewById(R.id.date);
            canclebtn = itemView.findViewById(R.id.cancelbtn);
        }
    }

    public void cancelDialog(final TicketViewHolder ticketViewHolder ,final Context context, final String trans_id){

        final AlertDialog.Builder canelTicket = new AlertDialog.Builder(context);
        canelTicket.setTitle("CANCEL TICKET");
        canelTicket.setMessage("Are you sure you want to cancel Ticket");
        canelTicket.setCancelable(true);
        canelTicket.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                requestQueue = Volley.newRequestQueue(ticketViewHolder.itemView.getContext());
                jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, path+trans_id, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");

                            if(status.equals("success")){
                                ticketViewHolder.canclebtn.setVisibility(View.INVISIBLE);
                               Toast.makeText(ticketViewHolder.itemView.getContext(),"Ticket Cancelled ", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(ticketViewHolder.itemView.getContext()," Cannot Cancel ticket ", Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("Cancel Error", error.getMessage());
                            }
                        });
                jsonObjectRequest.setTag("");
                requestQueue.add(jsonObjectRequest);
            }
        });
        canelTicket.show();
    }
}

