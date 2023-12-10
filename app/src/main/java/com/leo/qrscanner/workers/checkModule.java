package com.leo.qrscanner.workers;

import static com.google.mlkit.vision.codescanner.GmsBarcodeScanning.getClient;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.common.api.OptionalModuleApi;
import com.google.android.gms.common.moduleinstall.ModuleInstall;
import com.google.android.gms.common.moduleinstall.ModuleInstallClient;
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest;
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning;

public class checkModule {


    public void checkModuleinstall(Context context){

        ModuleInstallClient moduleInstallClient = ModuleInstall.getClient(context);

        OptionalModuleApi optionalModuleApi =  GmsBarcodeScanning.getClient(context);

        moduleInstallClient
                .areModulesAvailable(optionalModuleApi)
                .addOnSuccessListener(
                        response -> {
                            if (response.areModulesAvailable()) {
                                Toast.makeText(context, "Modules are present...", Toast.LENGTH_SHORT).show();
                                // Modules are present on the device...
                            } else {
                                // Modules are not present on the device...
                                Toast.makeText(context, "sending request to install worker ai ...", Toast.LENGTH_SHORT).show();


                                ModuleInstallRequest moduleInstallRequest = ModuleInstallRequest.newBuilder().addApi(optionalModuleApi).build();


                                moduleInstallClient.installModules(moduleInstallRequest)
                                        .addOnSuccessListener(
                                                response2 -> {
                                                    if (response2.areModulesAlreadyInstalled()) {
                                                        // Modules are already installed when the request is sent.
                                                    }
                                                })
                                        .addOnFailureListener(
                                                e -> {
                                                    // Handle failure...
                                                });
                            }


                        })
                .addOnFailureListener(
                        e -> {
                            // Handle failure…
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        });

    }

}
