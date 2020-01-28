package com.awintech.transpo;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.flutterwave.raveandroid.Meta;
import com.flutterwave.raveandroid.RaveConstants;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RavePayManager;
import com.flutterwave.raveandroid.responses.SubAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity {

    Random random = new Random();
    Button paybtn;
    EditText fname,sname,email,phone;
    String path = "https://transspo.com/savebookdetails";
    Intent intent;

    JsonObjectRequest request;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        requestQueue = Volley.newRequestQueue(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        paybtn = findViewById(R.id.buybtn);
        fname = findViewById(R.id.fname);
        sname = findViewById(R.id.sname);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }
    public void onPayClicked(){
        String first_name = fname.getText().toString().trim();
        String surname = sname.getText().toString().trim();
        String Email = email.getText().toString().trim();
        String Phone = phone.getText().toString();

//        intent.putExtra("COST",company_destination_data.get(i).getCost());
        String COST = getIntent().getStringExtra("COST");
        String FROM = getIntent().getStringExtra("FROM");
        String TO =  getIntent().getStringExtra("TO");
        String COMPANY_ID =  getIntent().getStringExtra("COMPANY_ID");
        final String COMPANY_NAME =  getIntent().getStringExtra("COMPANY_NAME");
        Double amount = Double.parseDouble(COST);
        String narration = "Transpo Ticket booked At "+ COMPANY_NAME+" for "+FROM+ " to " +TO ;
        List<SubAccount> subs = new ArrayList<>();
        SubAccount subAccount = new SubAccount();
        subAccount.setId("RS_B378808B636D2E6B3DE8A7D4396759AA");
        subAccount.setTransactionSplitRatio("1");
        subs.add(subAccount);

        List<Meta> metas = new ArrayList<>();
        Meta meta = new Meta("Message","Transpo Ticketing Services, the best for convenience");
        metas.add(meta);

        new RavePayManager(this)
                .setAmount(amount)
                .setCountry("GH")
                .setCurrency("GHS")
                .setEmail(Email)
                .setfName(first_name)
                .setlName(surname)
                .setNarration(narration)
                .setSubAccounts(subs)
                .setMeta(metas)
                .setPublicKey("FLWPUBK_TEST-fcd6c1083e643bb350717df8ac0cd7de-X")
                .setEncryptionKey("FLWSECK_TEST9cd291a37d31")
                .setTxRef( System.currentTimeMillis() + "Unique")
                .acceptAccountPayments(true)
                .acceptCardPayments(true)
                .acceptMpesaPayments(false)
                .acceptGHMobileMoneyPayments(true)
                .shouldDisplayFee(true)
                .showStagingLabel(false)
                .initialize();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {

            Log.i("DATA:", data.getStringExtra("response"));
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                ///////////////////////////////////////////////
                Bundle extras = data.getExtras();
                try {
                    JSONObject object = new JSONObject(extras.getString("response"));
                    Object meg = object.getJSONObject("data");
                    String transaction = ((JSONObject) meg).getString("id");
                    String flwRef = ((JSONObject) meg).getString("flwRef");
//                    Log.i("FLWREF: ", flwRef);
                    String paymentMethod = ((JSONObject) meg).getString("authModelUsed");
                    sendToDB("transpo."+transaction,flwRef,paymentMethod);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            else if (resultCode == RavePayActivity.RESULT_ERROR) {
            }
            else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
//                Toast.makeText(this, "CANCELLED " + message, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void registerUser() {
        String Phone = phone.getText().toString().trim();
        String Email = email.getText().toString().trim();
        String Fname = fname.getText().toString().trim();
        String Sname = sname.getText().toString().trim();

        if (Fname.isEmpty()) {
            fname.setError("Enter your first name");
            fname.requestFocus();
            return;
        }
        if (Fname.length() < 3) {
            fname.setError("Enter your first name correctly");
            fname.requestFocus();
            return;
        }
        if (Sname.isEmpty()) {
            sname.setError("Enter your surname");
            sname.requestFocus();
            return;
        }
        if (Sname.length() < 3) {
            sname.setError("Enter your surname correctly");
            sname.requestFocus();
            return;
        }
        if (Email.isEmpty()) {
            email.setError("Enter in your Email");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches())  {
            email.setError("Enter a valid Email");
            email.requestFocus();
            return;
        }

        if (Phone.isEmpty()) {
            phone.setError("Fill in your Phone Number");
            phone.requestFocus();
            return;
        }


        if (Phone.length() != 10) {
            phone.setError("Enter a valid Phone number");
            phone.requestFocus();
            return;
        }

        if (!Patterns.PHONE.matcher(Phone).matches()) {
            phone.setError("Enter a valid Phone Number");
            phone.requestFocus();
            return;
        }

        else {
            onPayClicked();
        }
    }

    public void sendToDB(String TransactionId, String flwRef, String paymentMethod){

        SharedPreferences sharedPreferences = this.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String user_phone = sharedPreferences.getString("phone","");


        String FIRSTNAME = fname.getText().toString();
        String SURNAME = sname.getText().toString();
        String PHONE = phone.getText().toString();
        String FROM = getIntent().getStringExtra("FROM");
        String TO =  getIntent().getStringExtra("TO");
        String COMPANY_ID =  getIntent().getStringExtra("COMPANY_ID");
        final String COMPANY_NAME =  getIntent().getStringExtra("COMPANY_NAME");
        final String BUS =  getIntent().getStringExtra("BUS");
        final String LOCATION =  getIntent().getStringExtra("LOCATION");
        final String SEAT =  getIntent().getStringExtra("SEAT");
        final String TIME =  getIntent().getStringExtra("TIME");
        final String DATE =  getIntent().getStringExtra("DATE");
        final String COST =  getIntent().getStringExtra("COST");
        final String DURATION =  getIntent().getStringExtra("DURATION");

        String TRANSACTION = TransactionId;

        //new code
        final JSONObject params = new JSONObject();
        try {
            params.put("t_from", FROM);
            params.put("t_to", TO);
            params.put("the_date", DATE);
            params.put("amount", COST);
            params.put("the_time", TIME);
            params.put("payment_method", paymentMethod);
            params.put("qrcode", flwRef);
            params.put("location", (LOCATION == null) ? LOCATION: "none");
            params.put("phone", user_phone);
            params.put("fullname", FIRSTNAME +" "+SURNAME);
            params.put("transaction_id", TRANSACTION);
            params.put("seat", SEAT);
            params.put("bus_no", BUS);
            params.put("company_name", COMPANY_NAME);
            params.put("company_id", COMPANY_ID);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        request = new JsonObjectRequest(Request.Method.POST, path,params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            String status = response.getString("status");
                            if((status.matches("success"))){
                                Toast.makeText(getApplication(), "Payment Was Successful!!!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplication(), MainActivity.class));
//                                Intent intent = new Intent(getActivity(), new TicketsFragment());
//                                getActivity().startActivity(intent);
                                send_sms("+233547785025","TICKET BOOK FROM ACCRA TO KUMASI.\n Transaction Id: SHAKDEODSK");
                                finishAffinity();
//                                    finish();
                            }
                            else{
                                Toast.makeText(getApplication(), "Payment Was failed!!!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finishAffinity();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== android.R.id.home){
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public void send_sms(String phone_no, String message){
        if(PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)){
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

            //Get the SmsManager instance and call the sendTextMessage method to send message
            SmsManager sms=SmsManager.getDefault();
            sms.sendTextMessage(phone_no, null, message, pi,null);

        }
        //Getting intent and PendingIntent instance

    }
}
