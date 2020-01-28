package com.awintech.transpo;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class Ticket_PreviewActivity extends AppCompatActivity {

    TextView from, to, company_name,seat,bus,fname,sname,phone,location,time,date,amount;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket__preview);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow);
        toolbar.setTitle("Ticket");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        to = findViewById(R.id.to);
        from = findViewById(R.id.from);
        company_name = findViewById(R.id.company_name);
        seat = findViewById(R.id.seat);
        bus = findViewById(R.id.bus);
        fname = findViewById(R.id.fname);
        sname = findViewById(R.id.sname);
        phone = findViewById(R.id.phone);
        location = findViewById(R.id.location);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        amount = findViewById(R.id.amount);

        from.setText(getIntent().getStringExtra("FROM"));
        to.setText(getIntent().getStringExtra("TO"));
        company_name.setText(getIntent().getStringExtra("COMPANY_NAME"));
        seat.setText(getIntent().getStringExtra("SEAT"));
        bus.setText(getIntent().getStringExtra("BUS"));
//        fname.setText(getIntent().getStringExtra("FIRST_NAME"));
//        sname.setText(getIntent().getStringExtra("SURNAME"));
//        phone.setText(getIntent().getStringExtra("PHONE"));
        location.setText(getIntent().getStringExtra("LOCATION"));
        time.setText(getIntent().getStringExtra("TIME"));
        date.setText(getIntent().getStringExtra("DATE"));
        amount.setText(getIntent().getStringExtra("AMOUNT"));
        String trans = getIntent().getStringExtra("TRANSACTION");

        ImageView image =(ImageView) findViewById(R.id.image);
        MultiFormatWriter multiFormatWriter =  new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(getIntent().getStringExtra("TRANSACTION"), BarcodeFormat.QR_CODE,500,500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            image.setImageBitmap(bitmap);
        }
        catch (WriterException e){
            e.printStackTrace();
        }
    }
}
