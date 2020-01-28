package com.awintech.transpo.login_signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.awintech.transpo.R;
import org.json.JSONException;
import org.json.JSONObject;

public class FresetpasswordActivity extends AppCompatActivity {

    Button fbtn ;
    EditText fconfirmpass,fpass;
    RequestQueue mqueue;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fresetpassword);

        mqueue = Volley.newRequestQueue(this);


        fconfirmpass = (EditText) findViewById(R.id.fconfirmpass);
        fpass = (EditText) findViewById(R.id.fpass);

        fbtn = (Button) findViewById(R.id.fbtn);

        fbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitNewPassword();
            }
        });

    }
    public void SubmitNewPassword(){
        final String password,confirmpassword;

        password = fpass.getText().toString();
        confirmpassword = fconfirmpass.getText().toString();

        if(password.isEmpty()) {
            fpass.setError("Fill in your password");
            fpass.requestFocus();
            return;
        }
        if(password.length()<6) {
            fpass.setError("Minimum length should be 6");
            fpass.requestFocus();
            return;
        }

        if(confirmpassword.isEmpty()){
            fconfirmpass.setError("Repeat your password");
            fconfirmpass.requestFocus();
            return;
        }
        if(!password.equals(confirmpassword))
        {
            fconfirmpass.setError("Password does not match");
            fconfirmpass.requestFocus();
            return;
        }

        else{
            // post request to api to change password
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setCancelable(false);
                progressDialog.show();

                // checks with server with the login details before allowing login

                String path = "https://transspo.com/resetpassword";

               String PH = getIntent().getStringExtra("PH");

                final JSONObject params = new JSONObject();
                try {
                    params.put("password", password);
                    params.put("phone", PH);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, path,params,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                try {

                                    String status = response.getString("status");
                                    String message = response.getString("message");
                                    if((status.matches("success"))){
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                        finishAffinity();
                                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                    }
                                    else{
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                        fpass.setText("");
                                        fconfirmpass.setText("");
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
                        Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                        fpass.setText("");
                        fconfirmpass.setText("");
                    }
                });

                mqueue.add(request);

        }
        }

    }

