package com.awintech.transpo;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Company_destinationsActivity extends AppCompatActivity {

    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    ImageView companyLogo;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    public JsonObjectRequest jsonObjectRequest;
    public static String path = "https://transspo.com/companydestination/ACCRA/KUMASI/1";

    List<Company_Destination_Data> data;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_destination);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        data = new ArrayList<Company_Destination_Data>();
        recyclerView = findViewById(R.id.company_destinationRecycler);
        companyLogo = findViewById(R.id.companyLogo);
        layoutManager = new LinearLayoutManager(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        setRequestQueue();
    }

    public void setRequestQueue(){
        String FROM = getIntent().getStringExtra("FROM");
        String TO =  getIntent().getStringExtra("TO");
        String COMPANY_ID =  getIntent().getStringExtra("COMPANY_ID");
       final String COMPANY_NAME =  getIntent().getStringExtra("COMPANY_NAME");

        requestQueue = Volley.newRequestQueue(this);
        String path = "https://transspo.com/companydestination/" ;
        String url = path+ FROM +"/"+ TO +"/" + COMPANY_ID;
        String TAG = "FROM_TO_DETAILS";

         toolbar.setTitle(COMPANY_NAME);
         toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow);
         toolbar.setTitleTextColor(getResources().getColor(R.color.white));
         toolbar.setSubtitleTextColor(getResources().getColor((R.color.white)));
         toolbar.setSubtitle(FROM + " to " + TO);


        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String status = response.getString("status");

                    if(status != null){
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), status,Toast.LENGTH_SHORT).show();

                        JSONObject MainjsonObject = response.getJSONObject("response");

                        String mycompanyId = MainjsonObject.getString("company_id") ;
                        String myfrom = MainjsonObject.getString("d_from");
                        String myto = MainjsonObject.getString("d_to");
                        String myduration = MainjsonObject.getString("journeyhrs");

                        //CHECKS IF COMPANY_ID IS SAME AS FROM INTENT(DESTINATION_activity class)
                        String companyId = MainjsonObject.getString("company_id") ;
                        if(!companyId.isEmpty()){


                            Company_Destination_Data company_destination_data = new Company_Destination_Data();

                            /////////////////////////change link to api Link
                            company_destination_data.setCompany_Logo(MainjsonObject.getString("image"));

                            JSONArray jsonResponse = MainjsonObject.getJSONArray("buses");
                            int count = 0;
                            while(count<jsonResponse.length()){


                                String c  = String.valueOf(count);
                                Toast.makeText(getApplicationContext(), c ,Toast.LENGTH_SHORT).show();
                                JSONObject jsonObject = jsonResponse.getJSONObject(count);

                                String mycost = jsonObject.getString("amount");
                                String mydate = jsonObject.getString("depart_date");
                                String mytime = jsonObject.getString("depart_time");
                                String myseat = jsonObject.getString("seat_left");
                                String mybusno = jsonObject.getString("bus_id");
                                String mylocation = "Tema station"; //CHANGE TO API DATA


                                company_destination_data.setCompany_Name(COMPANY_NAME);
                                company_destination_data.setCompany_Id(companyId);
                                company_destination_data.setDuration(myduration);
                                company_destination_data.setFrom(myfrom);
                                company_destination_data.setTo(myto);
                                company_destination_data.setCost(mycost);
                                company_destination_data.setDate(mydate);
                                company_destination_data.setTime(mytime);
                                company_destination_data.setSeat_No(myseat);
                                company_destination_data.setBus_No(mybusno);
                                company_destination_data.setDepart_Locaton(mylocation);


                                data.add(company_destination_data);
                                count++;
                            }
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Error loading data",Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recyclerView.setLayoutManager(layoutManager);
                adapter = new CompanyDestinationAdapter(getApplicationContext(),data);
                recyclerView.setAdapter(adapter);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        jsonObjectRequest.setTag(TAG);
        requestQueue.add(jsonObjectRequest);

    }


    @Override
    protected void onStop() {

        super.onStop();
    }
}
