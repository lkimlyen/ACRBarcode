package com.demo.scanacr.screen.history_pack;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class HistoryPackageModule {
    private final HistoryPackageContract.View CreateCodePackageView;

    public HistoryPackageModule(HistoryPackageContract.View CreateCodePackageView) {
        this.CreateCodePackageView = CreateCodePackageView;
    }

    @Provides
    @NonNull
    HistoryPackageContract.View provideHistoryPackageView() {
        return this.CreateCodePackageView;
    }
}

