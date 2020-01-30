package com.awintech.transpo.login_signup;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.awintech.transpo.R;

import org.json.JSONException;
import org.json.JSONObject;

public class FphoneActivity extends AppCompatActivity {

    Button ftokenbtn;
    EditText logphone;
    Dialog mydialog;
    RequestQueue mqueue;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fphone);

        ActivityCompat.requestPermissions(FphoneActivity.this,new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
        mqueue = Volley.newRequestQueue(this);
        logphone = findViewById(R.id.logphone);
        ftokenbtn = (Button) findViewById(R.id.ftokenbtn);
        ftokenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    public void Preview_dialog(final String tk, final String ph){

        mydialog = new Dialog(FphoneActivity.this);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mydialog.setContentView(R.layout.ftoken_dialog);

        Button ftbtn = (Button) mydialog.findViewById(R.id.ftbtn);
        final EditText tokentext =  mydialog.findViewById(R.id.tokentext);

        ftbtn.setEnabled(true);

        ftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tokentext.getText().toString().equals(tk)){
                    Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FresetpasswordActivity.class);
                    intent.putExtra("PH", ph);
                    startActivity(intent);
                    mydialog.cancel();
                }
                else {
                    mydialog.cancel();
                    Toast.makeText(getApplicationContext(),"Wrong Token", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mydialog.show();
    }

    public void loginUser(){
        final String loginephone = logphone.getText().toString().trim();

        if(loginephone.isEmpty()) {
            logphone.setError("Fill in your Phone number");
            logphone.requestFocus();
            return;
        }
        if(loginephone.length()!=10) {
            logphone.setError("Enter a valid Phone number");
            logphone.requestFocus();
            return;
        }

        if(!Patterns.PHONE.matcher(loginephone).matches()) {
            logphone.setError("Enter a valid Phone number");
            logphone.requestFocus();
            return;
        }
        else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.show();

            // checks with server with the login details before allowing login

            String path = "https://transspo.com/sendtoken";
            JSONObject params = new JSONObject();
            try {
                params.put("phone", loginephone);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, path,params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                String status = response.getString("status");
                                String token = response.getString("token");
                                if((status.matches("success"))){

                                    send_sms("TOKEN ALERT \n Transpo ticket Booker"+
                                            " \n Token: "+ token, loginephone);

                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(),"Check your SMS for Token", Toast.LENGTH_LONG).show();
                                    Toast.makeText(getApplicationContext(),token, Toast.LENGTH_LONG).show();
                                    Preview_dialog(token, loginephone);

                                }
                                else{
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();                             logphone.setText("");
                                    logphone.setText("");
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error occurred!", Toast.LENGTH_SHORT).show();                             logphone.setText("");
                    logphone.setText("");
                }
            });

            mqueue.add(request);

        }
    }

    public void send_sms(String message, String phone){
        ActivityCompat.requestPermissions(FphoneActivity.this,new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null,message, null,null);

        Log.i("trans", smsManager.toString());
    }

}
