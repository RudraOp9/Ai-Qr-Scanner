package com.leo.qrscanner;

import static com.leo.qrscanner.workers.dataType.TYPE_CALENDAR_EVENT;
import static com.leo.qrscanner.workers.dataType.TYPE_DRIVER_LICENSE;
import static com.leo.qrscanner.workers.dataType.TYPE_EMAIL;
import static com.leo.qrscanner.workers.dataType.TYPE_GEO;
import static com.leo.qrscanner.workers.dataType.TYPE_ISBN;
import static com.leo.qrscanner.workers.dataType.TYPE_PHONE;
import static com.leo.qrscanner.workers.dataType.TYPE_PRODUCT;
import static com.leo.qrscanner.workers.dataType.TYPE_SMS;
import static com.leo.qrscanner.workers.dataType.TYPE_TEXT;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.mlkit.vision.barcode.common.Barcode;
import com.leo.qrscanner.workers.ShareData;

public class showData extends AppCompatActivity {

    EditText textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);

        textView = findViewById(R.id.textView3);
        button = findViewById(R.id.btnPrimary);
        // ShareData sd = new ShareData();

        Intent i = getIntent();
        // String text = i.getStringExtra("text");
        // String btnText = i.getStringExtra("btnText");
        //  int btnType = i.getIntExtra("type",0);
        Bundle b = i.getBundleExtra("key");


        ShareData sd = b.getParcelable("barcode");


        Barcode barcode = sd.getBarcode();

        Log.d("tag", barcode.getRawValue());
        textView.setText(barcode.getRawValue());
        //  button.setText(btnText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  buttonAction(btnType);
                }
            });


    }

    private void buttonAction(int btnType) {

        switch (btnType) {
            case TYPE_EMAIL:
               // Intent intent = new Intent(this,Ema)
                break;


            case TYPE_PHONE:

                break;
            case Barcode.TYPE_WIFI:

                break;

            //TODO
            case Barcode.TYPE_URL:

                break;

            case Barcode.TYPE_CONTACT_INFO:

                break;

            case TYPE_CALENDAR_EVENT:

                break;

            case TYPE_GEO:

                break;


            case TYPE_DRIVER_LICENSE:

                break;

            case TYPE_SMS:

                break;

            case TYPE_ISBN:

                break;

            case TYPE_PRODUCT:

                break;

            case TYPE_TEXT:

                break;

            default:

        }

    }
}