package com.awintech.transpo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.awintech.transpo.login_signup.LoginActivity;
import com.awintech.transpo.login_signup.SignupActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements DashboardFragment.OnsendMessageListener, TicketsFragment.DashBoardListener,
        NavigationView.OnNavigationItemSelectedListener {

    private ActionBar toolbar;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    AlertDialog.Builder LogoutAlert;
    RequestQueue requestQueue;
    String HeaderName;
    BottomNavigationView navView;
    TextView navEmail;
    NavigationView navigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    toolbar.setTitle( "Dashboard");
                    Fragment fragment = new DashboardFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_dashboard:

                    toolbar.setTitle("Tickets");
                    Fragment fragment2 = new TicketsFragment();
                    loadFragment(fragment2);

                    return true;
                case R.id.navigation_notifications:

                    toolbar.setTitle("Notifications");
                    Fragment fragment3 = new NotificationFragment();
                    loadFragment(fragment3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = getSupportActionBar();
        toolbar.setTitle("Home");

        requestQueue = Volley.newRequestQueue(this);
        loadFragment(new DashboardFragment());
        navView = findViewById(R.id.nav_view);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        drawerLayout = findViewById(R.id.drawer);
        drawerLayout.setBackgroundColor(getResources().getColor(R.color.trans));
        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
//        drawerLayout.openDrawer(Gravity.START);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        navEmail = headerView.findViewById(R.id.headerEmail);


        SharedPreferences getPhoneNumber = this.getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
        String phoneNo = getPhoneNumber.getString("phone", "");
        getName(phoneNo);



//        SharedPreferences getEmailFullname = this.getSharedPreferences("storeEmailFullname", Context.MODE_PRIVATE);
//        String sharedEmail = getEmailFullname.getString("EMAIL", "");
//        String sharedFname = getEmailFullname.getString("FNAME", "");
//        String sharedSname = getEmailFullname.getString("SNAME", "");
//        navEmail.setText(sharedEmail);

    }
    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    @Override
    public void sendMessage(String message) {
        Log.i("",message);


    }


    @Override
    public void DBListener(int i, String name) {
        Toast.makeText(getApplicationContext(), i+ " " + name,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

           if (id == R.id.aboutus){
                startActivity(new Intent(getApplicationContext(), about.class));
                   Toast.makeText(this,"About us", Toast.LENGTH_SHORT).show();
           }
            if(id== R.id.logout){
                drawerLayout.closeDrawer(Gravity.START);
                Logout_dialog();
                Toast.makeText(this,"Logout coming soon", Toast.LENGTH_SHORT).show();
//                 MainActivity.this.finish();
            }
        if(id== R.id.help){
            Toast.makeText(this,"Help coming soon", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), help.class));
//             MainActivity.this.finish();
        }
        if(id== R.id.tickets){
            toolbar.setTitle("Tickets");
            Fragment fragment2 = new TicketsFragment();
            loadFragment(fragment2);
            drawerLayout.closeDrawer(Gravity.START);

            // HomeActivity.this.finish();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Logout_dialog(){

        LogoutAlert = new AlertDialog.Builder(this);
        LogoutAlert.setTitle("Logout Message");
        LogoutAlert.setMessage("Are you sure you want to Exit");

        LogoutAlert.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                drawerLayout.closeDrawer(Gravity.START);
            }
        });

        LogoutAlert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("checkbox", "false");
                editor.apply();

                Toast.makeText(getApplicationContext(),"Logging out...",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                MainActivity.this.finishAffinity();
            }
        });

        AlertDialog dialog = LogoutAlert.create();
        // Display the alert dialog on interface
        dialog.show();


    }

    private void getName(String phone){
        String path = "https://transspo.com/profile/" + phone;
        JsonObjectRequest  jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, path, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject user = response.getJSONObject("user");

                    if(user != null){
//                        progressDialog.dismiss();

                        HeaderName = user.getString("firstname");
                        String lastname = user.getString("lastname");
                        String fullname = HeaderName +" "+ lastname;
                        navEmail.setText(fullname);
                        Log.i("Headername: ", fullname);


                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Error loading data",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

}