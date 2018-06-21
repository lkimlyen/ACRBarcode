package com.demo.scanacr.screen.import_works;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class ImportWorksModule {
    private final ImportWorksContract.View ScanDeliveryView;

    public ImportWorksModule(ImportWorksContract.View ScanDeliveryView) {
        this.ScanDeliveryView = ScanDeliveryView;
    }

    @Provides
    @NonNull
    ImportWorksContract.View provideScanDeliveryView() {
        return this.ScanDeliveryView;
    }
}

