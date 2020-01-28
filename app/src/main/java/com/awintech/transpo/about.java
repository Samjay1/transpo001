package com.awintech.transpo;

import android.app.ProgressDialog;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class about extends AppCompatActivity {

    WebView aboutUs;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        aboutUs = findViewById(R.id.about_web);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
        aboutUs.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                progressDialog.dismiss();
                view.loadUrl("about:blank");
                Toast.makeText(getApplicationContext(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
            }

        });
        progressDialog.dismiss();
        aboutUs.loadUrl("http://ticketbooker.herokuapp.com/about1");

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
}
