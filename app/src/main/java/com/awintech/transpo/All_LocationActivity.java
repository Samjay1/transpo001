package com.awintech.transpo;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
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

public class All_LocationActivity extends AppCompatActivity {

    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;

    ProgressDialog progressDialog;

    RequestQueue requestQueue;
    public JsonObjectRequest jsonObjectRequest;

    String company_id;
    String company_name ;
    Toolbar toolbar;

   public List<All_Location_Data> all_location_data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all__location);
        toolbar = findViewById(R.id.toolbar1);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_keyboard_arrow));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Loading...");

        all_location_data = new ArrayList<>();
        recyclerView = findViewById(R.id.locationRecycler);
        progressDialog.show();
        requestDestinations();


    }

    public void requestDestinations(){
        company_id = getIntent().getStringExtra("COMPANY_ID");
        company_name = getIntent().getStringExtra("COMPANY_NAME");

       toolbar.setTitle(company_name);

        requestQueue = Volley.newRequestQueue(this);
        String path = "https://transspo.com/companydestinationdetails/" ;
        String url  = path.concat(company_id);// must change
        String TAG = "BUS_DESTINATIONS";


        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String status = response.getString("status");

                    if(status != null){
                            progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(), company_id+":ID"+status,Toast.LENGTH_SHORT).show();

                        JSONArray jsonResponse = response.getJSONArray("response");
                        int count = 0;
                        while(count<jsonResponse.length()){

                            JSONObject jsonObject = jsonResponse.getJSONObject(count);

                            //from_destination here
                            All_Location_Data data = new All_Location_Data();
                            data.setFrom(jsonObject.getString("d_from"));

                            // to_destination here
                            data.setTo(jsonObject.getString("d_to"));
                            data.setCompany_Id(company_id);
                            data.setCompany_Name(company_name);


                            all_location_data.add(data);
                            count++;
                        }
//                        adapter = new All_LocationAdapter(getApplicationContext(),all_location_data);
//                        recyclerView.setAdapter(adapter);
                    }

                    else{
                        Toast.makeText(getApplicationContext(), "Error loading data",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                adapter = new All_LocationAdapter(getApplicationContext(),all_location_data);
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
}
