package com.demo.scanacr.screen.scan_delivery;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class ScanDeliveryModule {
    private final ScanDeliveryContract.View ScanDeliveryView;

    public ScanDeliveryModule(ScanDeliveryContract.View ScanDeliveryView) {
        this.ScanDeliveryView = ScanDeliveryView;
    }

    @Provides
    @NonNull
    ScanDeliveryContract.View provideScanDeliveryView() {
        return this.ScanDeliveryView;
    }
}

