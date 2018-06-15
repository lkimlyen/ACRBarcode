package com.demo.scanacr.screen.print_stemp;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class PrintStempModule {
    private final PrintStempContract.View CreateCodePackageView;

    public PrintStempModule(PrintStempContract.View CreateCodePackageView) {
        this.CreateCodePackageView = CreateCodePackageView;
    }

    @Provides
    @NonNull
    PrintStempContract.View providePrintStempView() {
        return this.CreateCodePackageView;
    }
}

