package com.leo.qrscanner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;

import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.leo.qrscanner.workers.checkModule;

import java.io.IOException;

import java.util.Objects;


//made by leo.

public class MainActivity extends AppCompatActivity {

    GmsBarcodeScanner scanner;
    BarcodeScanner scanner2 = BarcodeScanning.getClient();
    checkModule checkModule = new checkModule();
    Button cameraScan, galleryScan;
    TextView textView;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






        cameraScan = findViewById(R.id.scanCamera);
        galleryScan =  findViewById(R.id.scanGallery);
        textView = findViewById(R.id.textView);
        textView.setVisibility(View.INVISIBLE);

        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(),  uri  -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.

                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        analyzeImage(uri);

                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });



        new BarcodeScannerOptions.Builder().enableAllPotentialBarcodes().build();


        cameraScan.setOnClickListener(v -> {
    new GmsBarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS).enableAutoZoom().build();

            scanner = GmsBarcodeScanning.getClient(MainActivity.this);
            startScan();
        });

        galleryScan.setOnClickListener(v -> {
        //imagePicker();
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts
                            .PickVisualMedia.ImageOnly
                            .INSTANCE).build());
        });






    } // on create ends here

    private void startScan() { // Scans the qr codes using google play service

        scanner
                .startScan()
                .addOnSuccessListener(
                        barcode -> {
                            // Task completed successfully
                            String rawValue = barcode.getRawValue();
                            int dataType = barcode.getValueType();

                            storeValueToSharedpref(rawValue,dataType);

                            Toast.makeText(this, rawValue, Toast.LENGTH_SHORT).show();
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(rawValue);
                        })
                .addOnCanceledListener(
                        () -> {
                            // Task canceled
                        })
                .addOnFailureListener(
                        e -> {
                            Log.d("onfailure" ,e.toString());
                            // Task failed with an exception
                        });
    }

    private void storeValueToSharedpref(String rawValue , int valueType) {//stores the value to shared prefrences

        //storing value , timestamp , value type ,
        long tsLong = System.currentTimeMillis()/1000;

        SharedPreferences sharedPreferences = getSharedPreferences("Qr data", MODE_PRIVATE);
        SharedPreferences.Editor power = sharedPreferences.edit();

        power.putString("qrValue",rawValue);
        power.putInt("qr type",valueType);
        power.putLong("create time",tsLong);
        power.apply();
    }


    private void analyzeImage(Uri uri) {

        InputImage image = null;
        try {
            image = InputImage.fromFilePath(MainActivity.this, uri);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (image != null) {
         scanner2.process(image)
                    .addOnSuccessListener(barcodes -> {
                        // Task completed successfully
                        // ...
                        for (Barcode barcode: barcodes) {
                          /*  Rect bounds = barcode.getBoundingBox();
                            Point[] corners = barcode.getCornerPoints();
*/
                            String rawValue = barcode.getRawValue();
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(rawValue);
                            Toast.makeText(MainActivity.this, rawValue, Toast.LENGTH_SHORT).show();

                            int valueType = barcode.getValueType();
                            String text= "";
                            String btnText;
                            // See API reference for complete list of supported types
                            switch (valueType) {
                                case Barcode.TYPE_WIFI:
                                    text = "**Wi-Fi Information:**\n";
                                    text += "SSID: " + Objects.requireNonNull(barcode.getWifi()).getSsid() + "\n";
                                    text += "Password: " + barcode.getWifi().getPassword() + "\n";
                                    text += "Encryption Type: " + barcode.getWifi().getEncryptionType();
                                    btnText = " Add WI-FI";
                                    showData(text,btnText);
                                    break;

                                case Barcode.TYPE_URL:
                                    text = "**URL:**\n";
                                    text += Objects.requireNonNull(barcode.getUrl()).getUrl();
                                    btnText = "Open in Browser";
                                    showData(text,btnText);
                                    Log.d("1","success");
                                    break;

                                case Barcode.TYPE_CONTACT_INFO:
                                    text = "**Contact Information:**\n";
                                    Barcode.ContactInfo contactInfo = barcode.getContactInfo();
                                    if (contactInfo != null) {
                                        text += "Name: " + contactInfo.getName() + "\n";
                                    }
                                    if (contactInfo != null) {
                                        text += "Email: " + contactInfo.getEmails() + "\n";
                                    }
                                    if (contactInfo != null) {
                                        text += "Phone: " + contactInfo.getPhones() + "\n";
                                    }
                                    if (contactInfo != null) {
                                        text += "Address: " + contactInfo.getAddresses();
                                    }
                                    btnText = "Add Contact";
                                    showData(text,btnText);
                                    break;

                                case Barcode.TYPE_CALENDAR_EVENT:
                                    text = "**Calendar Event:**\n";
                                    text += barcode.getRawValue();
                                    btnText = "add to calander";
                                    showData(text,btnText);
                                    break;

                                case Barcode.TYPE_GEO:
                                    text = "**Geo Point:**\n";
                                    Barcode.GeoPoint geoPoint = barcode.getGeoPoint();
                                    if (geoPoint != null) {
                                        text += "Latitude: " + geoPoint.getLat() + "\n";
                                    }
                                    if (geoPoint != null) {
                                        text += "Longitude: " + geoPoint.getLng();
                                    }
                                    btnText = "Open in Maps";
                                    showData(text,btnText);
                                    break;


                                case Barcode.TYPE_DRIVER_LICENSE:
                                    text = "**Driver License:**\n";
                                    Barcode.DriverLicense driverLicense = barcode.getDriverLicense();
                                    if (driverLicense != null) {
                                        text += "Name: " + driverLicense.getFirstName() + " " + driverLicense.getMiddleName()  +" " + driverLicense.getMiddleName() +"\n";
                                    }
                                    if (driverLicense != null) {
                                        text += "Address: " + driverLicense.getAddressStreet() +" " + driverLicense.getAddressCity() +" " + driverLicense.getAddressState() +" " + driverLicense.getAddressZip() + "\n";
                                    }
                                    if (driverLicense != null) {
                                        text += "License Number: " + driverLicense.getLicenseNumber() + "\n";
                                    }
                                    if (driverLicense != null) {
                                        text += "Issued Date: " + driverLicense.getIssueDate() + "\n";
                                    }
                                    if (driverLicense != null) {
                                        text += "Expiration Date: " + driverLicense.getExpiryDate();
                                    }
                                    btnText = "Rate 5 star";
                                    showData(text,btnText);
                                    break;

                                case Barcode.TYPE_SMS:
                                    text = "**SMS Message:**\n";
                                    Barcode.Sms sms = barcode.getSms();
                                    if (sms != null) {
                                        text += "Message: " + sms.getMessage() + "\n";
                                    }
                                    if (sms != null) {
                                        text += "Phone Number: " + sms.getPhoneNumber();
                                    }
                                    btnText = "Call this number";
                                    showData(text,btnText);
                                    break;

                                case Barcode.TYPE_ISBN:
                                    text = "**ISBN:**\n";
                                    text += barcode.getRawValue();
                                    btnText = "Rate 5 star";
                                    showData(text,btnText);
                                    break;

                                case Barcode.TYPE_PRODUCT:
                                    text = "**Product Information:**\n";
                                    text += barcode.getRawValue();
                                    btnText = " Search product in Browser";
                                    showData(text,btnText);
                                    break;

                                case Barcode.TYPE_TEXT:
                                    text = "**Plain Text:**\n";
                                    text += barcode.getRawValue();
                                    btnText = "Rate 5 star";
                                    showData(text,btnText);
                                    break;

                                default:
                                    text = "**Unknown Barcode Type:**\n";
                                    text += barcode.getRawValue();
                                    btnText = "Rate 5 star";
                                    showData(text,btnText);
                            }
                        }



                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Task failed with an exception
                            // ...
                        }
                    });
        }


    } // on create ends

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStart(){
        super.onStart();

        checkModule.checkModuleinstall(MainActivity.this);

    }

    public void copy (View v){
       String text =  textView.getText().toString().trim();
      // textView.setVisibility(View.VISIBLE);
        ClipboardManager clipboard = (ClipboardManager) MainActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }

    public void showData(String text2 , String buttonText2){

        Intent i = new Intent(MainActivity.this , showData.class);
        i.putExtra("text", text2);
        i.putExtra("btnText", buttonText2);
        startActivity(i);


    }

}