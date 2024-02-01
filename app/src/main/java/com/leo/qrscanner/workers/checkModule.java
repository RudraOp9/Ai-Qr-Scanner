package com.leo.qrscanner.workers;




import android.content.Context;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;


import com.google.android.gms.common.api.OptionalModuleApi;
import com.google.android.gms.common.moduleinstall.InstallStatusListener;
import com.google.android.gms.common.moduleinstall.ModuleInstall;
import com.google.android.gms.common.moduleinstall.ModuleInstallClient;
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest;
import com.google.android.gms.common.moduleinstall.ModuleInstallStatusUpdate;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;
import com.leo.qrscanner.R;


public class checkModule {

    Context context;
    AlertDialog alertDialog;
    TextView textView;
    ProgressBar progressBar;

    public void checkModuleinstall(Context context){


        this.context = context;
        displayAlertDialogue();
        ModuleInstallClient moduleInstallClient = ModuleInstall.getClient(context);
        OptionalModuleApi optionalModuleApi =  GmsBarcodeScanning.getClient(context);

        moduleInstallClient
                .areModulesAvailable(optionalModuleApi)
                .addOnSuccessListener(
                        response -> {
                            if (response.areModulesAvailable()) {
                                // Modules are present on the device...
                            } else {
                                alertDialog.show();
                                // Modules are not present on the device...



                                ModuleInstallRequest moduleInstallRequest = ModuleInstallRequest.newBuilder().addApi(optionalModuleApi).setListener(new InstallStatusListener() {
                                    @Override
                                    public void onInstallStatusUpdated(@NonNull ModuleInstallStatusUpdate moduleInstallStatusUpdate) {
                                        ModuleInstallStatusUpdate.ProgressInfo progressInfo = moduleInstallStatusUpdate.getProgressInfo();
                                        if (progressInfo != null) {
                                            progressBar.setMax((int) progressInfo.getTotalBytesToDownload());
                                            progressBar.setProgress((int) progressInfo.getBytesDownloaded());
                                        }
                                        if (moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_PENDING){
                                            textView.setText("Pending...");
                                        }else if(moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_DOWNLOADING){
                                            textView.setText("Downloading...");
                                        }else if(moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_INSTALLING){
                                            textView.setText("Installing...");
                                        } else if(moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_CANCELED){
                                            textView.setText("Canceled...");
                                        }else if(moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_FAILED){
                                            textView.setText("Failed check your connection...");
                                        }else if(moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_COMPLETED){
                                            textView.setText("Completed...");
                                        }else if(moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_DOWNLOAD_PAUSED){
                                            textView.setText("Download Paused...");
                                        }else if(moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_UNKNOWN){
                                            textView.setText("Working...");
                                        }
                                    }
                                }).build();


                              moduleInstallClient.installModules(moduleInstallRequest)
                                        .addOnSuccessListener(
                                                response2 -> {
                                                    if (response2.areModulesAlreadyInstalled()) {
                                                        // Modules are already installed when the request is sent.
                                                        Toast.makeText(context, "installed", Toast.LENGTH_SHORT).show();


                                                    }
                                                })
                                        .addOnFailureListener(
                                                e -> {
                                                    // Handle failure...
                                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                            }


                        })
                .addOnFailureListener(
                        e -> {
                            // Handle failure…
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        });

    }

    private void displayAlertDialogue(){

        alertDialog = new MaterialAlertDialogBuilder(context).setView(R.layout.alert_box).create();
        alertDialog.setContentView(R.layout.alert_box);
         //alertDialog.setView(R.layout.alert_box);

        alertDialog.setCancelable(false);
        textView = alertDialog.findViewById(R.id.alertText);
        progressBar= alertDialog.findViewById(R.id.progressBar);

        //Drawable progressDrawable = getResources().getDrawable(R.drawable.progress_draw);

        //progressBar.setProgressDrawable(progressDrawable);




    }

}
