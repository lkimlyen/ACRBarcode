package com.demo.scanacr.screen.detail_package;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class DetailPackageModule {
    private final DetailPackageContract.View CreateCodePackageView;

    public DetailPackageModule(DetailPackageContract.View CreateCodePackageView) {
        this.CreateCodePackageView = CreateCodePackageView;
    }

    @Provides
    @NonNull
    DetailPackageContract.View provideHistoryPackageView() {
        return this.CreateCodePackageView;
    }
}

