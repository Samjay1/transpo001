package com.awintech.transpo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

public class TicketsFragment extends Fragment {

    public RecyclerView.LayoutManager layoutManager;
    public RecyclerView.Adapter adapter;
    public RecyclerView recyclerView;
    Ticket_Data ticket_data;
    ProgressDialog progressDialog;


    public RequestQueue requestQueue;
    public JsonObjectRequest jsonObjectRequest;
    public static String path = "https://transspo.com/history/";

    List<Ticket_Data> data;
   TextView dbtext;
   public interface DashBoardListener{
       void DBListener(int i, String name);
   };

   private DashBoardListener dashBoardListener;
    public TicketsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tickets, container, false);

        data = new ArrayList<Ticket_Data>();
        recyclerView = view.findViewById(R.id.ticket_recycler);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences sharedPreferences = container.getContext().getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String phonedata = sharedPreferences.getString("phone", "");
        getdata(phonedata);

        return view;
    }

    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
//            Activity activity = (Activity) context;
            dashBoardListener = (DashBoardListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement DBListener");
        }

    }

public void getDataFromDB(){

         ticket_data = new Ticket_Data();
//
//    DBHelper dbHelper = new DBHelper(getContext());
//    ArrayList from = dbHelper.getAllContacts("d_from");
//    ArrayList to = dbHelper.getAllContacts("d_to");
//    ArrayList dates = dbHelper.getAllContacts("date");
//    ArrayList company_name = dbHelper.getAllContacts("company_name");

    DBHelper dbHelper = new DBHelper(getContext());
    List<Note> notte = dbHelper.getAllNotes();



    //filling the Ticket_data setters
    for(int i=0; i<notte.size(); i++){
        ticket_data.setCompanyName(notte.get(i).getCompanyName());
        ticket_data.setDate(notte.get(i).getDate());
        ticket_data.setFrom(notte.get(i).getFrom());
        ticket_data.setTo(notte.get(i).getTo());
        ticket_data.setCancelState(notte.get(i).getCancelState());
        ticket_data.setTransactionID(notte.get(i).getTransactionId());
        Log.i("NoTES DATA:", String.valueOf(notte.get(i).getCompanyName()));
        Log.i("NoTES DATA:", String.valueOf(notte.get(i).getCancelState()));
        Log.i("NoTES DATA:", String.valueOf(notte.get(i).getTransactionId()));

        data.add(ticket_data);
    }

    adapter = new TicketsAdapter(getContext(),data);

}

public void getdata(String Phone){
        String url = path+ Phone;
    progressDialog = new ProgressDialog(getContext());
    progressDialog.setMessage("Loading...");
    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressDialog.setCancelable(true);
    progressDialog.show();

    requestQueue = Volley.newRequestQueue(getContext());
    jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {

            try {

                JSONArray responseArray = response.getJSONArray("response");
                JSONObject MainjsonObject;
                if(responseArray != null){
                    progressDialog.dismiss();
                    for(int i = 0; i<responseArray.length(); i++){

                        ticket_data = new Ticket_Data();

                         MainjsonObject = responseArray.getJSONObject(i);

                        String myseat = MainjsonObject.getString("seat") ;
                        String mybus = MainjsonObject.getString("bus_no") ;
                        String myphone = MainjsonObject.getString("phone") ;
                        String myamount = MainjsonObject.getString("amount") ;
                        String mycompanyId = MainjsonObject.getString("company_id") ;
                        String mycompanyName = MainjsonObject.getString("company_name") ;
                        String myname = MainjsonObject.getString("nameofperson") ;
                        String myfrom = MainjsonObject.getString("t_from");
                        String myto = MainjsonObject.getString("t_to");
                        String mydate = MainjsonObject.getString("the_date");
                        String mytime = MainjsonObject.getString("the_time");
                        String mytrans = MainjsonObject.getString("transaction_id");
                        String mystatus = MainjsonObject.getString("status");

                        ticket_data.setSeat(myseat);
                        ticket_data.setBus_number(mybus);
                        ticket_data.setPhone(myphone);
                        ticket_data.setAmount(myamount);
                        ticket_data.setFirstname(myname);
                        ticket_data.setCompanyName(mycompanyName);
                        ticket_data.setCompany_id(mycompanyId);
                        ticket_data.setDate(mydate);
                        ticket_data.setFrom(myfrom);
                        ticket_data.setTo(myto);
                        ticket_data.setTime(mytime);
                        ticket_data.setTransactionID(mytrans);
                        ticket_data.setCancelState(mystatus);

                        data.add(ticket_data);

                        Log.i("Response", mycompanyName+mycompanyId);
                        Log.i("Response", MainjsonObject.toString());
                    }
                }
                else{
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Error loading data",Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }
            recyclerView.setLayoutManager(layoutManager);
            adapter = new TicketsAdapter(getContext(),data);
            recyclerView.setAdapter(adapter);
        }
    },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.i("volley  error", error.toString());
                }
            });
    requestQueue.add(jsonObjectRequest);
    }
}
