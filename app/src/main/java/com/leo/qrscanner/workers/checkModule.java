package com.leo.qrscanner.workers;




import static com.leo.qrscanner.R.string.*;

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

                                        if (moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_PENDING){
                                            textView.setText(pending);
                                        }else if(moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_DOWNLOADING){
                                            textView.setText(downloading);
                                            if (progressInfo != null) {
                                                progressBar.setMax((int) progressInfo.getTotalBytesToDownload());
                                                progressBar.setProgress((int) progressInfo.getBytesDownloaded());
                                            }
                                        }else if(moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_INSTALLING){
                                            textView.setText(installing);
                                        } else if(moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_CANCELED){
                                            textView.setText(canceled);
                                        }else if(moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_FAILED){
                                            textView.setText(failed_check_your_connection);

                                        }else if(moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_COMPLETED){
                                            textView.setText(completed);
                                            alertDialog.dismiss();
                                        }else if(moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_DOWNLOAD_PAUSED){
                                            textView.setText(download_paused);
                                        }else if(moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_UNKNOWN){
                                            textView.setText(working);
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
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });

    }

    private void displayAlertDialogue(){

        alertDialog = new MaterialAlertDialogBuilder(context).setView(R.layout.alert_box).create();
        alertDialog.setContentView(R.layout.alert_box);
        alertDialog.setCancelable(false);
        textView = alertDialog.findViewById(R.id.alertText);
        progressBar= alertDialog.findViewById(R.id.progressBar);
    }

}
