package com.awintech.transpo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
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


public class DashboardFragment extends Fragment {

    Activity activity;

    public RecyclerView.Adapter adapter ;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    RequestQueue requestQueue;
    public JsonObjectRequest jsonObjectRequest;
    ProgressDialog progressDialog;

    List<Dashboard_Company_Data> data;


    OnsendMessageListener onsendMessageListener;
    public TextView home;

    public  interface OnsendMessageListener{
        void sendMessage(String message);
    }

    public DashboardFragment() {
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

        onsendMessageListener.sendMessage("send");

        View view =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        home = view.findViewById(R.id.home);

        data = new ArrayList<Dashboard_Company_Data>();
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.recycler);

        requestVolley();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

         activity = (Activity) context;

        try {
            onsendMessageListener =(OnsendMessageListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "must override onsendmessageListener()");
        }

    }

    public void requestVolley(){
        /////////////////////////////
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();

        requestQueue = Volley.newRequestQueue(getContext());
        String path = "https://transspo.com/companies";
        String TAG = "BUS";

        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, path, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String status = response.getString("status");
                  if(status != null){
                        progressDialog.dismiss();

                        JSONArray jsonResponse = response.getJSONArray("response");
                        int count = 0;
                        while(count<jsonResponse.length()){

                            JSONObject jsonObject = jsonResponse.getJSONObject(count);
                            Dashboard_Company_Data dashboard_company_data = new Dashboard_Company_Data();

                            //bus companies Brand Name here
                            dashboard_company_data.setCompany_name(jsonObject.getString("name"));

                            // bus companies IDs here
                            dashboard_company_data.setCompany_Id(jsonObject.getString("id"));

                            // bus companies message here
                            dashboard_company_data.setCompany_message(jsonObject.getString("extra"));

                            // bus companies Images here
                            dashboard_company_data.setCompany_Image(jsonObject.getString("image"));

                            data.add(dashboard_company_data);
                            count++;
                        }
//
//
//                        dashboard_company_data.setCompany_Image(R.drawable.redbus);
//                        dashboard_company_data.setCompany_Image(R.drawable.greenbus);

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
                adapter = new RecyclerAdapter(getContext(), data);
                recyclerView.setAdapter(adapter);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        jsonObjectRequest.setTag("BUS");
        requestQueue.add(jsonObjectRequest);
        /////////////////////////////

    }

}
