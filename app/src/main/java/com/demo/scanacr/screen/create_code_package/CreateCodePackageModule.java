package com.demo.scanacr.screen.create_code_package;

import android.support.annotation.NonNull;

import dagger.Module;
import dagger.Provides;

/**
 * Created by MSI on 26/11/2017.
 */

@Module
public class CreateCodePackageModule {
    private final CreateCodePackageContract.View CreateCodePackageView;

    public CreateCodePackageModule(CreateCodePackageContract.View CreateCodePackageView) {
        this.CreateCodePackageView = CreateCodePackageView;
    }

    @Provides
    @NonNull
    CreateCodePackageContract.View provideCreateCodePackageView() {
        return this.CreateCodePackageView;
    }
}

