package com.leo.qrscanner.workers;

import static com.leo.qrscanner.R.string.canceled;
import static com.leo.qrscanner.R.string.completed;
import static com.leo.qrscanner.R.string.download_paused;
import static com.leo.qrscanner.R.string.downloading;
import static com.leo.qrscanner.R.string.failed_check_your_connection;
import static com.leo.qrscanner.R.string.installing;
import static com.leo.qrscanner.R.string.pending;
import static com.leo.qrscanner.R.string.working;

import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.common.api.OptionalModuleApi;
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


    public void checkModuleinstall(Context context){


        this.context = context;

        alertDialog = new MaterialAlertDialogBuilder(context).setView(R.layout.alert_box).create();
        alertDialog.setContentView(R.layout.alert_box);
        alertDialog.setCanceledOnTouchOutside(false);


        ModuleInstallClient moduleInstallClient = ModuleInstall.getClient(context);
        OptionalModuleApi optionalModuleApi = GmsBarcodeScanning.getClient(context);

        moduleInstallClient
                .areModulesAvailable(optionalModuleApi)
                .addOnSuccessListener(
                        response -> {
                            if (!response.areModulesAvailable()) {

                                alertDialog.show();
                                TextView textView;
                                ProgressBar progressBar;
                                textView = alertDialog.findViewById(R.id.alertText);
                                progressBar = alertDialog.findViewById(R.id.progressBar);
                                // Modules are not present on the device...
                                assert textView != null;
                                assert progressBar != null;


                                ModuleInstallRequest moduleInstallRequest = ModuleInstallRequest.newBuilder().addApi(optionalModuleApi).setListener(moduleInstallStatusUpdate -> {
                                    ModuleInstallStatusUpdate.ProgressInfo progressInfo = moduleInstallStatusUpdate.getProgressInfo();

                                    if (moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_PENDING) {

                                        textView.setText(pending);

                                    } else if (moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_DOWNLOADING) {

                                        textView.setText(downloading);

                                        if (progressInfo != null) {

                                            progressBar.setMax((int) progressInfo.getTotalBytesToDownload());
                                            progressBar.setProgress((int) progressInfo.getBytesDownloaded());
                                        }
                                    } else if (moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_INSTALLING) {

                                        textView.setText(installing);
                                    } else if (moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_CANCELED) {
                                        textView.setText(canceled);
                                    } else if (moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_FAILED) {
                                        textView.setText(failed_check_your_connection);

                                    } else if (moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_COMPLETED) {
                                        textView.setText(completed);
                                        alertDialog.dismiss();
                                    } else if (moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_DOWNLOAD_PAUSED) {
                                        textView.setText(download_paused);
                                    } else if (moduleInstallStatusUpdate.getInstallState() == ModuleInstallStatusUpdate.InstallState.STATE_UNKNOWN) {
                                        textView.setText(working);
                                    }
                                }).build();


                              moduleInstallClient.installModules(moduleInstallRequest)
                                        .addOnSuccessListener(
                                                response2 -> {
                                                    if (response2.areModulesAlreadyInstalled()) {
                                                        // Modules are already installed when the request is sent.
                                                        Toast.makeText(context, "installed", Toast.LENGTH_SHORT).show();
                                                        alertDialog.dismiss();


                                                    }
                                                })
                                        .addOnFailureListener(
                                                e -> {
                                                    // Handle failure...
                                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    alertDialog.dismiss();
                                                });
                            }


                        })
                .addOnFailureListener(
                        e -> {
                            // Handle failure…
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        });

    }



}
