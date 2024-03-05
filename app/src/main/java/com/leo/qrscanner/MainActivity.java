package com.leo.qrscanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner;
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.leo.qrscanner.workers.ShowDataFormat;
import com.leo.qrscanner.workers.checkModule;

import java.io.IOException;


//made by leo.

public class MainActivity extends AppCompatActivity {

   /*  recyclerView.addItemDecoration(
    DividerItemDecoration(
            baseContext,
            layoutManager.orientation
            )
        )*/

    GmsBarcodeScanner scanner;
    BarcodeScanner scanner2 = BarcodeScanning.getClient();
    checkModule checkModule = new checkModule();
    MaterialButton cameraScan, galleryScan;

    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    String rawValue = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checkModule.checkModuleinstall(this);


        cameraScan = findViewById(R.id.scanCamera);
        galleryScan = findViewById(R.id.scanGallery);


        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
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

        scanner.startScan()
                .addOnSuccessListener(
                        barcode -> {
                            // Task completed successfully
                            String rawValue = barcode.getRawValue();
                            checkType(barcode);
                            Toast.makeText(this, rawValue, Toast.LENGTH_SHORT).show();

                        })
                .addOnCanceledListener(
                        () -> {

                        })
                .addOnFailureListener(
                        e -> {
                            Log.d("onfailure", e.toString());
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
                        if (barcodes.isEmpty()) {
                            Toast.makeText(this, "no barcodes found", Toast.LENGTH_SHORT).show();
                        } else {
                            for (Barcode barcode : barcodes) {
                          /*  Rect bounds = barcode.getBoundingBox();
                            Point[] corners = barcode.getCornerPoints();
                            */
                                rawValue += barcode.getRawValue() + "\n \n";

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


    public void showData(String text2, String buttonText2, int dataType) {
        Intent i = new Intent(MainActivity.this, showData.class);
        i.putExtra("text", text2);
        i.putExtra("btnText", buttonText2);
        i.putExtra("type", dataType);
        startActivity(i);

    }

    private void checkType(Barcode barcode) {

        if (barcode == null) {
            return;
        }
        ShowDataFormat sd = new ShowDataFormat();
        Intent i = new Intent(MainActivity.this, showData.class);
        i.putStringArrayListExtra("styled", sd.styledString(barcode));
        i.putStringArrayListExtra("styledRaw", sd.styledRawString(barcode));
        i.putExtra("type", barcode.getValueType());
        i.putExtra("raw", barcode.getRawValue());
        startActivity(i);


    }

}