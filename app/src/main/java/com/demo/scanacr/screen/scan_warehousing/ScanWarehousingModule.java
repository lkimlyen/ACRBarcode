package com.demo.scanacr.screen.scan_warehousing;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class ScanWarehousingModule {
    private final ScanWarehousingContract.View CreateCodePackageView;

    public ScanWarehousingModule(ScanWarehousingContract.View CreateCodePackageView) {
        this.CreateCodePackageView = CreateCodePackageView;
    }

    @Provides
    @NonNull
    ScanWarehousingContract.View provideScanWarehousingView() {
        return this.CreateCodePackageView;
    }
}

