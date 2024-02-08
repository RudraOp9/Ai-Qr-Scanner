package com.leo.qrscanner;

import static com.leo.qrscanner.workers.dataType.TYPE_CALENDAR_EVENT;
import static com.leo.qrscanner.workers.dataType.TYPE_CONTACT_INFO;
import static com.leo.qrscanner.workers.dataType.TYPE_DRIVER_LICENSE;
import static com.leo.qrscanner.workers.dataType.TYPE_EMAIL;
import static com.leo.qrscanner.workers.dataType.TYPE_GEO;
import static com.leo.qrscanner.workers.dataType.TYPE_ISBN;
import static com.leo.qrscanner.workers.dataType.TYPE_PHONE;
import static com.leo.qrscanner.workers.dataType.TYPE_PRODUCT;
import static com.leo.qrscanner.workers.dataType.TYPE_SMS;
import static com.leo.qrscanner.workers.dataType.TYPE_TEXT;
import static com.leo.qrscanner.workers.dataType.TYPE_UNKNOWN;
import static com.leo.qrscanner.workers.dataType.TYPE_URL;
import static com.leo.qrscanner.workers.dataType.TYPE_WIFI;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.barcode.common.internal.BarcodeSource;
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

    String rawValue = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checkModule.checkModuleinstall(this );









        cameraScan = findViewById(R.id.scanCamera);
        galleryScan =  findViewById(R.id.scanGallery);
        textView = findViewById(R.id.textView);

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

                            checkType(barcode);


                            Toast.makeText(this, rawValue, Toast.LENGTH_SHORT).show();

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
                        if (barcodes.isEmpty()){
                            Toast.makeText(this, "no barcodes found", Toast.LENGTH_SHORT).show();
                        }else {
                            for (Barcode barcode : barcodes) {
                          /*  Rect bounds = barcode.getBoundingBox();
                            Point[] corners = barcode.getCornerPoints();
                            */
                                rawValue  += barcode.getRawValue()+"\n \n";
                                textView.setText(rawValue);
                                checkType(barcode);
                             }
                        }



                    })
                    .addOnFailureListener(e -> {
                        // Task failed with an exception
                        // ...
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
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



    }

    public void copy (View v){
        String text =  textView.getText().toString().trim();
        ClipboardManager clipboard = (ClipboardManager) MainActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }

    public void showData(String text2 , String buttonText2,int dataType){
        Intent i = new Intent(MainActivity.this , showData.class);
        i.putExtra("text", text2);
        i.putExtra("btnText", buttonText2);
        i.putExtra("type",dataType);
        startActivity(i);

    }

    private void checkType(Barcode barcode){

        if (barcode == null){
            return;
        }
        String text = "";
        String btnText;
        switch (barcode.getValueType()) {
            case TYPE_EMAIL:
                text = "**E-Mail Information:**\n";
                Barcode.Email email = barcode.getEmail();
                assert email != null;
                text += email.getType() + "\n";
                text += email.getAddress() + "\n";
                text += email.getSubject() + "\n";
                text += email.getBody() + "\n";
                break;



            case TYPE_PHONE:
                text= "**Phone Number :**\n";
                Barcode.Phone phone = barcode.getPhone();
                text+= "" + phone.getNumber() + "\n";
                text+= "" + phone.getType() + "\n";
                break;
            case Barcode.TYPE_WIFI:
                text = "**Wi-Fi Information:**\n";
                text += "SSID: " + Objects.requireNonNull(barcode.getWifi()).getSsid() + "\n";
                text += "Password: " + barcode.getWifi().getPassword() + "\n";
                text += "Encryption Type: " + barcode.getWifi().getEncryptionType();
                btnText = " Add WI-FI";
                showData(text, btnText,TYPE_WIFI);
                break;

            case Barcode.TYPE_URL:
                text = "**URL:**\n";
                text +=  "" +Objects.requireNonNull(barcode.getUrl()).getUrl();
                btnText = "Open in Browser";
                showData(text, btnText,TYPE_URL);
                Log.d("1", "success");
                break;

            case Barcode.TYPE_CONTACT_INFO:
                text = "**Contact Information:**\n";
                Barcode.ContactInfo contactInfo = barcode.getContactInfo();

                assert contactInfo != null;
                text += "Name: " + contactInfo.getName() + "\n";
                text += "Email: " + contactInfo.getEmails() + "\n";
                text += "Phone: " + contactInfo.getPhones() + "\n";
                text += "Address: " + contactInfo.getAddresses();

                btnText = "Add Contact";
                showData(text, btnText,TYPE_CONTACT_INFO);
                break;

            case Barcode.TYPE_CALENDAR_EVENT:
                text = "**Calendar Event:**\n";
                text += barcode.getRawValue();
                btnText = "add to calander";
                showData(text, btnText,TYPE_CALENDAR_EVENT);
                break;

            case Barcode.TYPE_GEO:
                text = "**Geo Point:**\n";
                Barcode.GeoPoint geoPoint = barcode.getGeoPoint();

                assert geoPoint != null;
                text += "Latitude: " + geoPoint.getLat() + "\n";
                text += "Longitude: " + geoPoint.getLng();

                btnText = "Open in Maps";
                showData(text, btnText,TYPE_GEO);
                break;


            case Barcode.TYPE_DRIVER_LICENSE:
                text = "**Driver License:**\n";
                Barcode.DriverLicense driverLicense = barcode.getDriverLicense();

                assert driverLicense != null;
                text += "Name: " + driverLicense.getFirstName() + " " + driverLicense.getMiddleName() + " " + driverLicense.getMiddleName() + "\n";
                text += "Address: " + driverLicense.getAddressStreet() + " " + driverLicense.getAddressCity() + " " + driverLicense.getAddressState() + " " + driverLicense.getAddressZip() + "\n";
                text += "License Number: " + driverLicense.getLicenseNumber() + "\n";
                text += "Issued Date: " + driverLicense.getIssueDate() + "\n";
                text += "Expiration Date: " + driverLicense.getExpiryDate();
                btnText = "Rate 5 star";
                showData(text, btnText,TYPE_DRIVER_LICENSE);
                break;

            case Barcode.TYPE_SMS:
                text = "**SMS Message:**\n";
                Barcode.Sms sms = barcode.getSms();

                assert sms != null;
                text += "Message: " + sms.getMessage() + "\n";
                text += "Phone Number: " + sms.getPhoneNumber();

                btnText = "Call this number";
                showData(text, btnText,TYPE_SMS);
                break;

            case Barcode.TYPE_ISBN:
                text = "**ISBN:**\n";
                text += barcode.getRawValue();
                btnText = "Rate 5 star";
                showData(text, btnText,TYPE_ISBN);
                break;

            case Barcode.TYPE_PRODUCT:
                text = "**Product Information:**\n";
                text += barcode.getRawValue();
                btnText = " Search product in Browser";
                showData(text, btnText,TYPE_PRODUCT);
                break;

            case Barcode.TYPE_TEXT:
                text = "**Plain Text:**\n";
                text += barcode.getRawValue();
                btnText = "Rate 5 star";
                showData(text, btnText,TYPE_TEXT);
                break;

            default:
                text = "**Unknown Barcode :**\n";
                text += barcode.getRawValue();
                btnText = "Rate 5 star";
                showData(text, btnText,TYPE_UNKNOWN);
        }
    }

}