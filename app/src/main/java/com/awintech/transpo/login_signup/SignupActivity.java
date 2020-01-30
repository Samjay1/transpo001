package com.awintech.transpo.login_signup;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.HashSet;

public class SignupActivity extends AppCompatActivity{

    private EditText fname,sname,sphone,spassword,scpassword,email;
    Button signupbtn;
    TextView logintxt;

    RequestQueue requestQueue;
    public JsonObjectRequest request, profile_request;
    String path = "https://transspo.com/signup";
    String profile_path = "https://transspo.com/savebasicprofile";

    ProgressDialog progressDialog;
    Dialog tokenDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        requestQueue = Volley.newRequestQueue(this);
        ActivityCompat.requestPermissions(SignupActivity.this ,new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);

        fname = findViewById(R.id.fname);
        sname = findViewById(R.id.sname);
        sphone = findViewById(R.id.sphone);
        email = findViewById(R.id.semail);
        spassword = findViewById(R.id.spassword);
        scpassword = findViewById(R.id.scpassword);
        signupbtn = findViewById(R.id.sbtn);
        logintxt = findViewById(R.id.lforgot_password);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignUpClicked(v);
            }
        });

        logintxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginClicked(v);
            }
        });

    }
    public void onSignUpClicked(View v){
        registerUser();
//        startActivity(new Intent(this,LoginActivity.class));
    }

    public void onLoginClicked(View v){
        startActivity(new Intent(this, LoginActivity.class));
        finishAffinity();
    }


    //authenticate the signup details before sending to the server

    public void registerUser(){
        final String phone = sphone.getText().toString().trim();
        final String Fname = fname.getText().toString().trim();
        final String Sname = sname.getText().toString().trim();
        final String Email = email.getText().toString().trim();
        String password = spassword.getText().toString().trim();
        String confirmpassword = scpassword.getText().toString().trim();

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


        if(phone.isEmpty()) {
            sphone.setError("Fill in your Phone Number");
            sphone.requestFocus();
            return;
        }


        if(phone.length()!=10) {
            sphone.setError("Enter a valid Phone number");
            sphone.requestFocus();
            return;
        }

        if(!Patterns.PHONE.matcher(phone).matches()) {
            sphone.setError("Enter a valid Phone Number");
            sphone.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            spassword.setError("Fill in your password");
            spassword.requestFocus();
            return;
        }
        if(password.length()<6) {
            spassword.setError("Minimum length should be 6");
            spassword.requestFocus();
            return;
        }

        if(confirmpassword.isEmpty()){
            scpassword.setError("Repeat your password");
            scpassword.requestFocus();
            return;
        }
        if(!password.equals(confirmpassword))
        {
            scpassword.setError("Password does not match");
            scpassword.requestFocus();
            return;
        }

        else {
            //usong the volley POST request method, the phone number and password will be sent to the database
            progressDialog = new ProgressDialog(SignupActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(true);
            progressDialog.show();

            JSONObject params = new JSONObject();
            try {
                params.put("phone", phone);
                params.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            request = new JsonObjectRequest(Request.Method.POST, path,params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                final String tk = response.getString("token");
                                send_sms("TOKEN ALERT \n Transpo ticket Booker"+
                                        " \n Token: "+ tk, phone);

                                tokenDialog = new Dialog(SignupActivity.this);
                                tokenDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                tokenDialog.setContentView(R.layout.token_dialog);
                                tokenDialog.setCanceledOnTouchOutside(true);
                                Button ftbtn = tokenDialog.findViewById(R.id.ftbtn);
                                final EditText tokentext =  tokenDialog.findViewById(R.id.tokentext);

                                progressDialog.dismiss();
                                tokenDialog.show();
                                Toast.makeText(getApplicationContext(),"Check your SMS for Token", Toast.LENGTH_LONG).show();

                                ftbtn.setEnabled(true);
                                ftbtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        if(tokentext.getText().toString().equals(tk)){
                                            Toast.makeText(getApplicationContext(),"Successful: You Login now", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                            sendBasicProfile(Fname,Sname,Email,phone);
                                            tokenDialog.cancel();
                                            startActivity(intent);

                                        }
                                        else{
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Token is wrong", Toast.LENGTH_SHORT).show();
                                    resetForm();
                                }
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Phone Number already exits", Toast.LENGTH_SHORT).show();
                                resetForm();

                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();

                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    resetForm();
                }
            });

            requestQueue.add(request);

        }
    }

    public void resetForm(){
        sphone.setText("");
        spassword.setText("");
        scpassword.setText("");

        sphone.setHint("Phone Number");
        spassword.setHint("Password");
        scpassword.setHint("Confirm password");
    }


    private void sendBasicProfile(String fname,String sname, String email, String phone){
        JSONObject params = new JSONObject();
        try {
            params.put("phone", phone);
            params.put("email", email);
            params.put("firstname", fname);
            params.put("lastname", sname);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        profile_request = new JsonObjectRequest(Request.Method.POST, profile_path,params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            String status = response.getString("status");
                            final String tk ;
                            if((status.matches("success"))){
                                Log.i("PROFILE UPDATE:", status);
                            }
                            else{
                                Log.i("PROFILE UPDATE:", "failed");
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
        requestQueue.add(profile_request);
    }

    public void send_sms(String message, String phone){
        ActivityCompat.requestPermissions(SignupActivity.this,new String[]{Manifest.permission.READ_SMS, Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phone, null,message, null,null);


        Log.i("trans", smsManager.toString());
    }
}
