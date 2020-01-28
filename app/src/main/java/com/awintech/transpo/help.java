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

public class help extends AppCompatActivity {

    WebView help;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        help = findViewById(R.id.help_web);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.show();
        help.setWebViewClient(new WebViewClient(){
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

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                progressDialog.dismiss();
                try {
                    help.loadUrl("javascript:(window.onload = function() { " +
                            "(elem1 = document.getElementsByClass('navbar-inverse')); elem.parentNode.removeChild(elem1); " +
                            "(elem2 = document.getElementsByTagName('footer')); elem2.parentNode.removeChild(elem2); " +
                            "})()");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        progressDialog.dismiss();
        help.loadUrl("https://transpoweb.herokuapp.com/help");

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
